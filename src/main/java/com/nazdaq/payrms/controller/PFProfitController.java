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
import com.nazdaq.payrms.beans.PFProfitBean;
import com.nazdaq.payrms.beans.PFProfitSheet;
import com.nazdaq.payrms.model.PFProfit;
import com.nazdaq.payrms.model.Company;
import com.nazdaq.payrms.model.Employee;
import com.nazdaq.payrms.model.PaymentMonth;
import com.nazdaq.payrms.service.CommonService;
import com.nazdaq.payrms.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class PFProfitController implements Constants{
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${cc.email.addresss}")
	String ccEmailAddresss;
	
	@Value("${common.email.address}")
	String commonEmailAddress;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newAddPFProfitForm.htm", method = RequestMethod.GET)
	public ModelAndView pfProfitForm (@ModelAttribute("pfProfitForm") PFProfit pfProfitBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}		
		
		List<PaymentMonth> paymentMonthList = (List<PaymentMonth>)(Object)
				commonService.getAllObjectList("PaymentMonth");		
		
		Collections.reverse(paymentMonthList);
		
		Map <String, Object> model = new HashMap<String, Object>();	
		
		model.put("paymentMonthList", paymentMonthList);	
		return new ModelAndView("addPFProfitForm", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/generateAddPFProfitForm.htm", method = RequestMethod.POST)
	public ModelAndView generateAddPFProfit (@ModelAttribute("pfProfitForm") PFProfit pfProfitBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}			
		
		Company company = (Company) session.getAttribute("company");
		PaymentMonth paymentMonth = (PaymentMonth) commonService.getAnObjectByAnyUniqueColumn("PaymentMonth", "id", pfProfitBean.getPaymentMonthId().toString());
		
		List<PFProfit> pfProfitList = (List<PFProfit>)(Object) 
				commonService.getObjectListByTwoColumn("PFProfit", "employee.company.id", company.getId().toString(), "paymentMonth.id", pfProfitBean.getPaymentMonthId().toString());
		
		if(pfProfitList != null && pfProfitList.size() > 0) {	
			if(pfProfitList.get(0).getFinalSubmit().equals("1")) {
				return this.pfProfitFinalList(pfProfitBean, principal, session, paymentMonth);
			} else {
				return this.pfProfitList(pfProfitBean, principal, session, paymentMonth);
			}
			
		} else {
			List<Employee> employeeList = (List<Employee>)(Object) commonService.getObjectListByTwoColumn("Employee", "company.id", company.getId().toString(), "status", ACTIVE);
			PFProfit pfProfit = null;
			if(employeeList != null && employeeList.size() > 0) {
				for (Employee employee : employeeList) {
					pfProfit = new PFProfit();
					pfProfit.setEmployee(employee);
					pfProfit.setCreatedBy(principal.getName());
					pfProfit.setCreatedDate(new Date());
					pfProfit.setPaymentMonth(paymentMonth);
					try{
						commonService.saveOrUpdateModelObjectToDB(pfProfit);
					}catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}						
			return this.pfProfitList(pfProfitBean, principal, session, paymentMonth);
		}
		
	}
	
	
	public ModelAndView pfProfitList (@ModelAttribute("pfProfitForm") PFProfit pfProfitBean, Principal principal, HttpSession session, PaymentMonth paymentMonth) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}		
		
		Map <String, Object> model = new HashMap<String, Object>();	
		model.put("paymentMonth", paymentMonth);
		return new ModelAndView("pfProfitFormList", model);		
	}
	
	public ModelAndView pfProfitFinalList (PFProfit pfProfitBean, Principal principal, HttpSession session, PaymentMonth paymentMonth) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}		
		
		Map <String, Object> model = new HashMap<String, Object>();	
		model.put("paymentMonth", paymentMonth);
		return new ModelAndView("pfProfitFinalList", model);		
	}
	
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/getMonthlyPFProfitList.htm"}, method = RequestMethod.POST)
	private @ResponseBody String getMonthlyPFProfitList(@RequestBody String jesonString, Principal principal, HttpSession session) 
			throws JsonGenerationException, JsonMappingException, Exception {
		String toJson = "";
		Gson gson = new Gson();
		Company company = (Company) session.getAttribute("company");
		
		PFProfit pfProfit = gson.fromJson(jesonString, PFProfit.class);
		
		List<PFProfit> pfProfitList = (List<PFProfit>)(Object) 
				commonService.getObjectListByTwoColumn("PFProfit", "employee.company.id", company.getId().toString(), "paymentMonth.id", pfProfit.getPaymentMonthId().toString());
		
		int i = 1;
		for (PFProfit pf : pfProfitList) {
			pf.setSlNo(i);
			pf.setRecid(pf.getId());
			i++;
		}
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		toJson = ow.writeValueAsString(pfProfitList);
		return toJson;
	}
	
	@RequestMapping(value = {"/saveMonthlyPFProfitList.htm"}, method = RequestMethod.POST)
	private @ResponseBody String PFProfit(@RequestBody String jesonString, Principal principal) 
			throws JsonGenerationException, JsonMappingException, Exception {
		String toJson = "";
		String result = "";
		Gson gson = new Gson();
		PFProfitBean pfProfitBean = gson.fromJson(jesonString, PFProfitBean.class);		
		List<PFProfitSheet>	pfProfitList = pfProfitBean.getPfProfitSheetList();
		Date now = new Date();
		String loginUser = principal.getName();
		if(pfProfitList.size() > 0 ) {
			for (PFProfitSheet pf : pfProfitList) {
				PFProfit pfDb = (PFProfit) commonService.getAnObjectByAnyUniqueColumn("PFProfit", "id", pf.getRecid().toString());
				
				
				if(pf.getGivenDate() != null) {	
					pfDb.setGivenDate(pf.getGivenDate());
				}
				
				if(pf.getPfAmount() != null) {	
					pfDb.setPfAmount(pf.getPfAmount());
				}
				
				if(pf.getRemarks() != null) {	
					pfDb.setRemarks(pf.getRemarks());
				}
				
				pfDb.setModifiedBy(loginUser);
				pfDb.setModifiedDate(now);
				
				commonService.saveOrUpdateModelObjectToDB(pfDb);
			}
			result = "success";
		}
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		toJson = ow.writeValueAsString(result);
		return toJson;
	}
	
	//pfProfitFinalSubmit
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/pfProfitFinalSubmit.htm", method = RequestMethod.POST)
	public ModelAndView pfProfitFinalSubmit (PFProfit pfProfitBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}			
		
		Company company = (Company) session.getAttribute("company");
		PaymentMonth paymentMonth = (PaymentMonth) commonService.getAnObjectByAnyUniqueColumn("PaymentMonth", "id", pfProfitBean.getPaymentMonthId().toString());
		
		List<PFProfit> pfProfitList = (List<PFProfit>)(Object) 
				commonService.getObjectListByTwoColumn("PFProfit", "employee.company.id", company.getId().toString(), "paymentMonth.id", pfProfitBean.getPaymentMonthId().toString());
		
		for (PFProfit pf : pfProfitList) {
			pf.setFinalSubmit("1");
			commonService.saveOrUpdateModelObjectToDB(pf);
		}
		
		return this.pfProfitFinalList(pfProfitBean, principal, session, paymentMonth);
	}
}
