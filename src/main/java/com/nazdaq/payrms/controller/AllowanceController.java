package com.nazdaq.payrms.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.nazdaq.payrms.beans.AllowanceBean;
import com.nazdaq.payrms.beans.AllowanceSheet;
import com.nazdaq.payrms.model.AdditionalAllowance;
import com.nazdaq.payrms.model.Company;
import com.nazdaq.payrms.model.Employee;
import com.nazdaq.payrms.model.PaymentMonth;
import com.nazdaq.payrms.service.CommonService;
import com.nazdaq.payrms.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class AllowanceController implements Constants{
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${cc.email.addresss}")
	String ccEmailAddresss;
	
	@Value("${common.email.address}")
	String commonEmailAddress;
	
	@RequestMapping(value = "/newAddAllowanceForm.htm", method = RequestMethod.GET)
	public ModelAndView allowanceForm (@ModelAttribute("allowanceForm") AdditionalAllowance additionalAllowanceBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}		
		
		List<PaymentMonth> paymentMonthList = (List<PaymentMonth>)(Object)
				commonService.getAllObjectList("PaymentMonth");		
		
		Collections.reverse(paymentMonthList);
		
		Map <String, Object> model = new HashMap<String, Object>();	
		
		model.put("paymentMonthList", paymentMonthList);	
		return new ModelAndView("addAllowanceForm", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/generateAddAllowanceForm.htm", method = RequestMethod.POST)
	public ModelAndView generateAddAllowance (@ModelAttribute("allowanceForm") AdditionalAllowance additionalAllowanceBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}			
		
		Company company = (Company) session.getAttribute("company");
		PaymentMonth paymentMonth = (PaymentMonth) commonService.getAnObjectByAnyUniqueColumn("PaymentMonth", "id", additionalAllowanceBean.getPaymentMonthId().toString());
		
		List<AdditionalAllowance> allowanceList = (List<AdditionalAllowance>)(Object) 
				commonService.getObjectListByTwoColumn("AdditionalAllowance", "employee.company.id", company.getId().toString(), "paymentMonth.id", additionalAllowanceBean.getPaymentMonthId().toString());
		
		if(allowanceList != null && allowanceList.size() > 0) {	
			if(allowanceList.get(0).getFinalSubmit().equals("1")) {
				return this.allowanceFinalList(additionalAllowanceBean, principal, session, paymentMonth);
			} else {
				return this.allowanceList(additionalAllowanceBean, principal, session, paymentMonth);
			}
			
		} else {
			List<Employee> employeeList = (List<Employee>)(Object) commonService.getObjectListByTwoColumn("Employee", "company.id", company.getId().toString(), "status", ACTIVE);
			AdditionalAllowance allowance = null;
			if(employeeList != null && employeeList.size() > 0) {
				for (Employee employee : employeeList) {
					allowance = new AdditionalAllowance();
					allowance.setEmployee(employee);
					allowance.setCreatedBy(principal.getName());
					allowance.setCreatedDate(new Date());
					allowance.setPaymentMonth(paymentMonth);
					try{
						commonService.saveOrUpdateModelObjectToDB(allowance);
					}catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}						
			return this.allowanceList(additionalAllowanceBean, principal, session, paymentMonth);
		}
		
	}
	
	
	public ModelAndView allowanceList (@ModelAttribute("allowanceForm") AdditionalAllowance additionalAllowanceBean, Principal principal, HttpSession session, PaymentMonth paymentMonth) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}		
		
		Map <String, Object> model = new HashMap<String, Object>();	
		model.put("paymentMonth", paymentMonth);
		return new ModelAndView("allowanceFormList", model);		
	}
	
	public ModelAndView allowanceFinalList (AdditionalAllowance additionalAllowanceBean, Principal principal, HttpSession session, PaymentMonth paymentMonth) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}		
		
		Map <String, Object> model = new HashMap<String, Object>();	
		model.put("paymentMonth", paymentMonth);
		return new ModelAndView("allowanceFinalList", model);		
	}
	
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/getMonthlyAllowanceList.htm"}, method = RequestMethod.POST)
	private @ResponseBody String getMonthlyAllowanceList(@RequestBody String jesonString, Principal principal, HttpSession session) 
			throws JsonGenerationException, JsonMappingException, Exception {
		String toJson = "";
		Gson gson = new Gson();
		Company company = (Company) session.getAttribute("company");
		
		AdditionalAllowance allowance = gson.fromJson(jesonString, AdditionalAllowance.class);
		
		List<AdditionalAllowance> allowanceList = (List<AdditionalAllowance>)(Object) 
				commonService.getObjectListByTwoColumn("AdditionalAllowance", "employee.company.id", company.getId().toString(), "paymentMonth.id", allowance.getPaymentMonthId().toString());
		
		int i = 1;
		for (AdditionalAllowance additionalAllowance : allowanceList) {
			additionalAllowance.setSlNo(i);
			additionalAllowance.setRecid(additionalAllowance.getId());
			i++;
		}
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		toJson = ow.writeValueAsString(allowanceList);
		return toJson;
	}
	
	@RequestMapping(value = {"/saveMonthlyAllowanceList.htm"}, method = RequestMethod.POST)
	private @ResponseBody String saveMonthlyAllowanceList(@RequestBody String jesonString, Principal principal) 
			throws JsonGenerationException, JsonMappingException, Exception {
		String toJson = "";
		String result = "";
		Gson gson = new Gson();
		AllowanceBean allowanceBean = gson.fromJson(jesonString, AllowanceBean.class);		
		List<AllowanceSheet>	allowanceList = allowanceBean.getAllowanceSheetList();
		Date now = new Date();
		String loginUser = principal.getName();
		if(allowanceList.size() > 0 ) {
			for (AllowanceSheet allowance : allowanceList) {
				AdditionalAllowance addAllowanceDb = (AdditionalAllowance) commonService.getAnObjectByAnyUniqueColumn("AdditionalAllowance", "id", allowance.getRecid().toString());
				
				
				if(allowance.getAllowArear() != null) {	
					addAllowanceDb.setAllowArear(allowance.getAllowArear());
				}
				
				if(allowance.getAllowOthers() != null) {	
					addAllowanceDb.setAllowOthers(allowance.getAllowOthers());
				}
				
				if(allowance.getInchargeAllow() != null) {	
					addAllowanceDb.setInchargeAllow(allowance.getInchargeAllow());
				}
				
				if(allowance.getOvertime() != null) {	
					addAllowanceDb.setOvertime(allowance.getOvertime());
				}	
				
				if(allowance.getRemarks() != null) {	
					addAllowanceDb.setRemarks(allowance.getRemarks());
				}
				
				addAllowanceDb.setModifiedBy(loginUser);
				addAllowanceDb.setModifiedDate(now);
				
				commonService.saveOrUpdateModelObjectToDB(addAllowanceDb);
			}
			result = "success";
		}
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		toJson = ow.writeValueAsString(result);
		return toJson;
	}
	
	//allowanceFinalSubmit
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/allowanceFinalSubmit.htm", method = RequestMethod.POST)
	public ModelAndView allowanceFinalSubmit (AdditionalAllowance additionalAllowanceBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}			
		
		Company company = (Company) session.getAttribute("company");
		PaymentMonth paymentMonth = (PaymentMonth) commonService.getAnObjectByAnyUniqueColumn("PaymentMonth", "id", additionalAllowanceBean.getPaymentMonthId().toString());
		
		List<AdditionalAllowance> allowanceList = (List<AdditionalAllowance>)(Object) 
				commonService.getObjectListByTwoColumn("AdditionalAllowance", "employee.company.id", company.getId().toString(), "paymentMonth.id", additionalAllowanceBean.getPaymentMonthId().toString());
		
		for (AdditionalAllowance additionalAllowance : allowanceList) {
			additionalAllowance.setFinalSubmit("1");
			commonService.saveOrUpdateModelObjectToDB(additionalAllowance);
		}
		
		return this.allowanceFinalList(additionalAllowanceBean, principal, session, paymentMonth);
	}
}
