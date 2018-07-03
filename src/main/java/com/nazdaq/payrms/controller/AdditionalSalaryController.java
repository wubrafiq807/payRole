package com.nazdaq.payrms.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import com.nazdaq.payrms.model.AdditionalSalary;
import com.nazdaq.payrms.model.Company;
import com.nazdaq.payrms.model.Employee;
import com.nazdaq.payrms.model.PaymentMonth;
import com.nazdaq.payrms.service.CommonService;
import com.nazdaq.payrms.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class AdditionalSalaryController implements Constants{

	@Autowired
	private CommonService commonService;
	
	@SuppressWarnings("unused")
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${cc.email.addresss}")
	String ccEmailAddresss;
	
	@Value("${common.email.address}")
	String commonEmailAddress;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editAdditionalSalary.htm", method = RequestMethod.GET)
	public ModelAndView editAdditionalSalary(@ModelAttribute("additionalSalaryForm") AdditionalSalary additionalSalaryBean, Principal principal, HttpSession session,HttpServletRequest request) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Company company = (Company) session.getAttribute("company");
		
		List<PaymentMonth> paymentMonthList = (List<PaymentMonth>)(Object)
				commonService.getAllObjectList("PaymentMonth");		
		
		List<Employee> employeeList = (List<Employee>)(Object)
				commonService.getObjectListByTwoColumn("Employee", "company.id", company.getId().toString(), "status", "1");
		AdditionalSalary aditionalSalary=(AdditionalSalary) commonService.getAnObjectByAnyUniqueColumn("AdditionalSalary", "id", request.getParameter("id"));
		Collections.reverse(paymentMonthList);
		
		Map <String, Object> model = new HashMap<String, Object>();	
		model.put("edit", true);
		model.put("aditionalSalary", aditionalSalary);
		model.put("paymentMonthList", paymentMonthList);	
		model.put("employeeList", employeeList);
		return new ModelAndView("addAdditionalSalary", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newAdditionalSalaryForm.htm", method = RequestMethod.GET)
	public ModelAndView additionalSalaryForm (@ModelAttribute("additionalSalaryForm") AdditionalSalary additionalSalaryBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Company company = (Company) session.getAttribute("company");
		
		List<PaymentMonth> paymentMonthList = (List<PaymentMonth>)(Object)
				commonService.getAllObjectList("PaymentMonth");		
		
		List<Employee> employeeList = (List<Employee>)(Object)
				commonService.getObjectListByTwoColumn("Employee", "company.id", company.getId().toString(), "status", "1");
		
		Collections.reverse(paymentMonthList);
		
		Map <String, Object> model = new HashMap<String, Object>();	
		
		model.put("paymentMonthList", paymentMonthList);	
		model.put("employeeList", employeeList);
		return new ModelAndView("addAdditionalSalary", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/saveAdditionalSalaryOnPage.htm"}, method = RequestMethod.POST)
	private @ResponseBody String saveAdditionalSalaryOnPage(@RequestBody String jesonString, Principal principal, HttpSession session) 
			throws JsonGenerationException, JsonMappingException, Exception {
		String toJson = "";
		String response = "";
		Gson gson = new Gson();
				
		AdditionalSalary additionalSalary = gson.fromJson(jesonString, AdditionalSalary.class);
		
		List<AdditionalSalary> additionalSalaryList = (List<AdditionalSalary>)(Object) 
				commonService.getObjectListByTwoColumn("AdditionalSalary", "employee.id", additionalSalary.getEmployeeId().toString(), "paymentMonth.id", additionalSalary.getPaymentMonthId().toString());
		
		if(additionalSalaryList.size() > 0) {
			AdditionalSalary additionalSalaryDb = additionalSalaryList.get(0);
			additionalSalaryDb.setRemarks(additionalSalary.getRemarks());
			additionalSalaryDb.setConsolitedSalary(additionalSalary.getConsolitedSalary());
			additionalSalaryDb.setOthersSalary(additionalSalary.getOthersSalary());
			additionalSalaryDb.setModifiedBy(principal.getName());
			additionalSalaryDb.setModifiedDate(new Date());
			commonService.saveOrUpdateModelObjectToDB(additionalSalaryDb);
			response = "Additional Salary Update Successfully";
		} else {
			Employee employee = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "id", additionalSalary.getEmployeeId().toString());
			PaymentMonth paymentMonth = (PaymentMonth) commonService.getAnObjectByAnyUniqueColumn("PaymentMonth", "id", additionalSalary.getPaymentMonthId().toString());
			
			AdditionalSalary additionalSalaryNew = new AdditionalSalary();
			additionalSalaryNew.setEmployee(employee);
			additionalSalaryNew.setPaymentMonth(paymentMonth);	
			
			additionalSalaryNew.setConsolitedSalary(additionalSalary.getConsolitedSalary());
			additionalSalaryNew.setOthersSalary(additionalSalary.getOthersSalary());
			
			additionalSalaryNew.setCreatedBy(principal.getName());			
			additionalSalaryNew.setCreatedDate(new Date());
			additionalSalaryNew.setRemarks(additionalSalary.getRemarks());
			commonService.saveOrUpdateModelObjectToDB(additionalSalaryNew);
			response = "Additional Salary Save Successfully.";
		}
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		toJson = ow.writeValueAsString(response);
		return toJson;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveAdditionalSalary.htm", method = RequestMethod.POST)
	public ModelAndView saveAdditionalSalary (@ModelAttribute("additionalSalaryForm") AdditionalSalary additionalSalaryBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		List<AdditionalSalary> additionalSalaryList = (List<AdditionalSalary>)(Object) 
				commonService.getObjectListByTwoColumn("AdditionalSalary", "employee.id", additionalSalaryBean.getEmployeeId().toString(), "paymentMonth.id", additionalSalaryBean.getPaymentMonthId().toString());	
		
		if(additionalSalaryList.size() > 0) {
			AdditionalSalary additionalSalaryDb = additionalSalaryList.get(0);
			additionalSalaryDb.setRemarks(additionalSalaryBean.getRemarks());
			additionalSalaryDb.setConsolitedSalary(additionalSalaryBean.getConsolitedSalary());
			additionalSalaryDb.setOthersSalary(additionalSalaryBean.getOthersSalary());
			additionalSalaryDb.setModifiedBy(principal.getName());
			additionalSalaryDb.setModifiedDate(new Date());
			commonService.saveOrUpdateModelObjectToDB(additionalSalaryDb);	
		} else {
			Employee employee = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "id", additionalSalaryBean.getEmployeeId().toString());
			PaymentMonth paymentMonth = (PaymentMonth) commonService.getAnObjectByAnyUniqueColumn("PaymentMonth", "id", additionalSalaryBean.getPaymentMonthId().toString());
			AdditionalSalary additionalSalaryNew = new AdditionalSalary();
			additionalSalaryNew.setEmployee(employee);
			additionalSalaryNew.setPaymentMonth(paymentMonth);	
			
			additionalSalaryNew.setConsolitedSalary(additionalSalaryBean.getConsolitedSalary());
			additionalSalaryNew.setOthersSalary(additionalSalaryBean.getOthersSalary());
			
			additionalSalaryNew.setCreatedBy(principal.getName());			
			additionalSalaryNew.setCreatedDate(new Date());
			additionalSalaryNew.setRemarks(additionalSalaryBean.getRemarks());
			commonService.saveOrUpdateModelObjectToDB(additionalSalaryNew);			
		}
				
		
		return new ModelAndView("redirect:/additionalSalaryList.htm?paymentMonthId="+additionalSalaryBean.getPaymentMonthId().toString());		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newAdditionalSalaryListForm.htm", method = RequestMethod.GET)
	public ModelAndView additionalSalaryListForm (@ModelAttribute("additionalSalaryForm") AdditionalSalary additionalSalaryBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		List<PaymentMonth> paymentMonthList = (List<PaymentMonth>)(Object)
				commonService.getAllObjectList("PaymentMonth");		
		
			Collections.reverse(paymentMonthList);
		
		Map <String, Object> model = new HashMap<String, Object>();	
		
		model.put("paymentMonthList", paymentMonthList);	
		return new ModelAndView("additionalSalaryListForm", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/additionalSalaryList.htm", method = RequestMethod.GET)
	public ModelAndView additionalSalaryList (@ModelAttribute("additionalSalaryForm") AdditionalSalary additionalSalaryBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Company company = (Company) session.getAttribute("company");
		
		List<AdditionalSalary> additionalSalaryList = (List<AdditionalSalary>)(Object) 
				commonService.getObjectListByTwoColumn("AdditionalSalary", "employee.company.id", company.getId().toString(), "paymentMonth.id", additionalSalaryBean.getPaymentMonthId().toString());	
		
		
		Map <String, Object> model = new HashMap<String, Object>();	
		
		model.put("additionalSalaryList", additionalSalaryList);	
		
		return new ModelAndView("additionalSalaryList", model);
	}
	
}
