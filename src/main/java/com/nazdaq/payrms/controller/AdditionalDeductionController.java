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
import com.nazdaq.payrms.beans.DeductionBean;
import com.nazdaq.payrms.beans.DeductionSheet;
import com.nazdaq.payrms.model.AdditionalDeduction;
import com.nazdaq.payrms.model.Company;
import com.nazdaq.payrms.model.Employee;
import com.nazdaq.payrms.model.PaymentMonth;
import com.nazdaq.payrms.service.CommonService;
import com.nazdaq.payrms.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class AdditionalDeductionController implements Constants{

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${cc.email.addresss}")
	String ccEmailAddresss;
	
	@Value("${common.email.address}")
	String commonEmailAddress;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newAddDeductionForm.htm", method = RequestMethod.GET)
	public ModelAndView deductionForm (@ModelAttribute("deductionForm") AdditionalDeduction additionalDeductionBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}		
		
		List<PaymentMonth> paymentMonthList = (List<PaymentMonth>)(Object)
				commonService.getAllObjectList("PaymentMonth");		
		
		Collections.reverse(paymentMonthList);
		
		Map <String, Object> model = new HashMap<String, Object>();	
		
		model.put("paymentMonthList", paymentMonthList);	
		return new ModelAndView("addDeductionForm", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/generateAddDeductionForm.htm", method = RequestMethod.POST)
	public ModelAndView generateAddDeduction (@ModelAttribute("deductionForm") AdditionalDeduction additionalDeductionBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}			
		
		Company company = (Company) session.getAttribute("company");
		PaymentMonth paymentMonth = (PaymentMonth) commonService.getAnObjectByAnyUniqueColumn("PaymentMonth", "id", additionalDeductionBean.getPaymentMonthId().toString());
		
		List<AdditionalDeduction> deductionList = (List<AdditionalDeduction>)(Object) 
				commonService.getObjectListByTwoColumn("AdditionalDeduction", "employee.company.id", company.getId().toString(), "paymentMonth.id", additionalDeductionBean.getPaymentMonthId().toString());
		
		if(deductionList != null && deductionList.size() > 0) {	
			if(deductionList.get(0).getFinalSubmit().equals("1")) {
				return this.deductionFinalList(additionalDeductionBean, principal, session, paymentMonth);
			} else {
				return this.deductionList(additionalDeductionBean, principal, session, paymentMonth);
			}
			
		} else {
			List<Employee> employeeList = (List<Employee>)(Object) commonService.getObjectListByTwoColumn("Employee", "company.id", company.getId().toString(), "status", ACTIVE);
			AdditionalDeduction deduction = null;
			if(employeeList != null && employeeList.size() > 0) {
				for (Employee employee : employeeList) {
					deduction = new AdditionalDeduction();
					deduction.setEmployee(employee);
					deduction.setCreatedBy(principal.getName());
					deduction.setCreatedDate(new Date());
					deduction.setPaymentMonth(paymentMonth);
					try{
						commonService.saveOrUpdateModelObjectToDB(deduction);
					}catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}						
			return this.deductionList(additionalDeductionBean, principal, session, paymentMonth);
		}
		
	}
	
	
	public ModelAndView deductionList (@ModelAttribute("deductionForm") AdditionalDeduction additionalDeductionBean, Principal principal, HttpSession session, PaymentMonth paymentMonth) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}		
		
		Map <String, Object> model = new HashMap<String, Object>();	
		model.put("paymentMonth", paymentMonth);
		return new ModelAndView("deductionFormList", model);		
	}
	
	public ModelAndView deductionFinalList (AdditionalDeduction additionalDeductionBean, Principal principal, HttpSession session, PaymentMonth paymentMonth) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}		
		
		Map <String, Object> model = new HashMap<String, Object>();	
		model.put("paymentMonth", paymentMonth);
		return new ModelAndView("deductionFinalList", model);		
	}
	
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/getMonthlyDeductionList"}, method = RequestMethod.POST)
	private @ResponseBody String getMonthlyAllowanceList(@RequestBody String jesonString, Principal principal, HttpSession session) 
			throws JsonGenerationException, JsonMappingException, Exception {
		String toJson = "";
		Gson gson = new Gson();
		Company company = (Company) session.getAttribute("company");
		
		AdditionalDeduction deduction = gson.fromJson(jesonString, AdditionalDeduction.class);
		
		List<AdditionalDeduction> deductionList = (List<AdditionalDeduction>)(Object) 
				commonService.getObjectListByTwoColumn("AdditionalDeduction", "employee.company.id", company.getId().toString(), "paymentMonth.id", deduction.getPaymentMonthId().toString());
		
		int i = 1;
		for (AdditionalDeduction additionalDeduction : deductionList) {
			additionalDeduction.setSlNo(i);
			additionalDeduction.setRecid(additionalDeduction.getId());
			i++;
		}
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		toJson = ow.writeValueAsString(deductionList);
		return toJson;
	}
	
	@RequestMapping(value = {"/saveMonthlyDeductionList"}, method = RequestMethod.POST)
	private @ResponseBody String saveMonthlyDeductionList(@RequestBody String jesonString, Principal principal) 
			throws JsonGenerationException, JsonMappingException, Exception {
		String toJson = "";
		String result = "";
		Gson gson = new Gson();
		DeductionBean deductionBean = gson.fromJson(jesonString, DeductionBean.class);		
		List<DeductionSheet>	deductionList = deductionBean.getDeductionSheetList();
		Date now = new Date();
		String loginUser = principal.getName();
		if(deductionList.size() > 0 ) {
			for (DeductionSheet deductionSheet : deductionList) {
				AdditionalDeduction addDeductionDb = (AdditionalDeduction) commonService.getAnObjectByAnyUniqueColumn("AdditionalDeduction", "id", deductionSheet.getRecid().toString());
				
				
				if(deductionSheet.getFineAmount() != null) {	
					addDeductionDb.setFineAmount(deductionSheet.getFineAmount());
				}
				
				if(deductionSheet.getDeductStamp() != null) {	
					addDeductionDb.setDeductStamp(deductionSheet.getDeductStamp());
				}
				
				
				if(deductionSheet.getRemarks() != null) {	
					addDeductionDb.setRemarks(deductionSheet.getRemarks());
				}
				
				addDeductionDb.setModifiedBy(loginUser);
				addDeductionDb.setModifiedDate(now);
				
				commonService.saveOrUpdateModelObjectToDB(addDeductionDb);
			}
			result = "success";
		}
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		toJson = ow.writeValueAsString(result);
		return toJson;
	}
	
	//deductionFinalSubmit
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/deductionFinalSubmit.htm", method = RequestMethod.POST)
	public ModelAndView deductionFinalSubmit (AdditionalDeduction additionalDeductionBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}			
		
		Company company = (Company) session.getAttribute("company");
		PaymentMonth paymentMonth = (PaymentMonth) commonService.getAnObjectByAnyUniqueColumn("PaymentMonth", "id", additionalDeductionBean.getPaymentMonthId().toString());
		
		List<AdditionalDeduction> deductionList = (List<AdditionalDeduction>)(Object) 
				commonService.getObjectListByTwoColumn("AdditionalDeduction", "employee.company.id", company.getId().toString(), "paymentMonth.id", additionalDeductionBean.getPaymentMonthId().toString());
		
		for (AdditionalDeduction additionalDeduction : deductionList) {
			additionalDeduction.setFinalSubmit("1");
			commonService.saveOrUpdateModelObjectToDB(additionalDeduction);
		}
		
		return this.deductionFinalList(additionalDeductionBean, principal, session, paymentMonth);
	}
}
