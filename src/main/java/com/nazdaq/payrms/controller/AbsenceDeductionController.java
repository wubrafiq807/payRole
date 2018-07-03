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
import com.nazdaq.payrms.model.AbsenceDeduction;
import com.nazdaq.payrms.model.Company;
import com.nazdaq.payrms.model.Employee;
import com.nazdaq.payrms.model.PaymentMonth;
import com.nazdaq.payrms.service.CommonService;
import com.nazdaq.payrms.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class AbsenceDeductionController implements Constants{

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${cc.email.addresss}")
	String ccEmailAddresss;
	
	@Value("${common.email.address}")
	String commonEmailAddress;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editAbsenceDeduction.htm", method = RequestMethod.GET)
	public ModelAndView editAbsenceDeduction(@ModelAttribute("absenceDeductionForm") AbsenceDeduction absenceDeductionBean, Principal principal, HttpSession session,HttpServletRequest request) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Company company = (Company) session.getAttribute("company");
		
		List<PaymentMonth> paymentMonthList = (List<PaymentMonth>)(Object)
				commonService.getAllObjectList("PaymentMonth");		
		
		List<Employee> employeeList = (List<Employee>)(Object)
				commonService.getObjectListByTwoColumn("Employee", "company.id", company.getId().toString(), "status", "1");
		AbsenceDeduction absenceDeduction=(AbsenceDeduction) commonService.getAnObjectByAnyUniqueColumn("AbsenceDeduction", "id", request.getParameter("id"));
		Collections.reverse(paymentMonthList);
		
		Map <String, Object> model = new HashMap<String, Object>();	
		model.put("edit", true);
		model.put("absenceDeduction", absenceDeduction);
		model.put("paymentMonthList", paymentMonthList);	
		model.put("employeeList", employeeList);
		return new ModelAndView("addAbsenceDeduction", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newAbsenceDeductionorm.htm", method = RequestMethod.GET)
	public ModelAndView absenceDeductionForm (@ModelAttribute("absenceDeductionForm") AbsenceDeduction absenceDeductionBean, Principal principal, HttpSession session) {		
		
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
		return new ModelAndView("addAbsenceDeduction", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/saveAbsenceDeductionOnPage.htm"}, method = RequestMethod.POST)
	private @ResponseBody String saveAbsenceDeductionOnPage(@RequestBody String jesonString, Principal principal, HttpSession session) 
			throws JsonGenerationException, JsonMappingException, Exception {
		String toJson = "";
		String response = "";
		Gson gson = new Gson();
		
		AbsenceDeduction absenceDeduction = gson.fromJson(jesonString, AbsenceDeduction.class);
		
		List<AbsenceDeduction> absenceDeductionList = (List<AbsenceDeduction>)(Object) 
				commonService.getObjectListByTwoColumn("AbsenceDeduction", "employee.id", absenceDeduction.getEmployeeId().toString(), "paymentMonth.id", absenceDeduction.getPaymentMonthId().toString());
		
		if(absenceDeductionList.size() > 0) {
			AbsenceDeduction absenceDeductionDb = absenceDeductionList.get(0);
			absenceDeductionDb.setRemarks(absenceDeduction.getRemarks());
			absenceDeductionDb.setDeductAmount(absenceDeduction.getDeductAmount());
			absenceDeductionDb.setDeductDays(absenceDeduction.getDeductDays());
			absenceDeductionDb.setModifiedBy(principal.getName());
			absenceDeductionDb.setModifiedDate(new Date());
			commonService.saveOrUpdateModelObjectToDB(absenceDeductionDb);
			response = "Absence Deduction Update Successfully";
		} else {
			Employee employee = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "id", absenceDeduction.getEmployeeId().toString());
			PaymentMonth paymentMonth = (PaymentMonth) commonService.getAnObjectByAnyUniqueColumn("PaymentMonth", "id", absenceDeduction.getPaymentMonthId().toString());
			
			AbsenceDeduction absenceDeductionNew = new AbsenceDeduction();
			absenceDeductionNew.setEmployee(employee);
			absenceDeductionNew.setPaymentMonth(paymentMonth);	
			
			absenceDeductionNew.setDeductAmount(absenceDeduction.getDeductAmount());
			absenceDeductionNew.setDeductDays(absenceDeduction.getDeductDays());
			
			absenceDeductionNew.setCreatedBy(principal.getName());			
			absenceDeductionNew.setCreatedDate(new Date());
			absenceDeductionNew.setRemarks(absenceDeduction.getRemarks());
			commonService.saveOrUpdateModelObjectToDB(absenceDeductionNew);
			response = "Absence Deduction Save Successfully.";
		}
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		toJson = ow.writeValueAsString(response);
		return toJson;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveAbsenceDeduction.htm", method = RequestMethod.POST)
	public ModelAndView saveAbsenceDeduction (@ModelAttribute("absenceDeductionForm") AbsenceDeduction absenceDeductionBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		List<AbsenceDeduction> absenceDeductionList = (List<AbsenceDeduction>)(Object) 
				commonService.getObjectListByTwoColumn("AbsenceDeduction", "employee.id", absenceDeductionBean.getEmployeeId().toString(), "paymentMonth.id", absenceDeductionBean.getPaymentMonthId().toString());	
		
		if(absenceDeductionList.size() > 0) {
			AbsenceDeduction absenceDeductionDb = absenceDeductionList.get(0);
			absenceDeductionDb.setRemarks(absenceDeductionBean.getRemarks());
			
			absenceDeductionDb.setDeductAmount(absenceDeductionBean.getDeductAmount());
			absenceDeductionDb.setDeductDays(absenceDeductionBean.getDeductDays());
			
			absenceDeductionDb.setModifiedBy(principal.getName());
			absenceDeductionDb.setModifiedDate(new Date());
			commonService.saveOrUpdateModelObjectToDB(absenceDeductionDb);	
		} else {
			Employee employee = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "id", absenceDeductionBean.getEmployeeId().toString());
			PaymentMonth paymentMonth = (PaymentMonth) commonService.getAnObjectByAnyUniqueColumn("PaymentMonth", "id", absenceDeductionBean.getPaymentMonthId().toString());
			AbsenceDeduction absenceDeductionNew = new AbsenceDeduction();
			absenceDeductionNew.setEmployee(employee);
			absenceDeductionNew.setPaymentMonth(paymentMonth);	
			
			absenceDeductionNew.setDeductAmount(absenceDeductionBean.getDeductAmount());
			absenceDeductionNew.setDeductDays(absenceDeductionBean.getDeductDays());
			
			absenceDeductionNew.setCreatedBy(principal.getName());			
			absenceDeductionNew.setCreatedDate(new Date());
			absenceDeductionNew.setRemarks(absenceDeductionBean.getRemarks());
			commonService.saveOrUpdateModelObjectToDB(absenceDeductionNew);			
		}
				
		
		return new ModelAndView("redirect:/absenceDeductionList.htm?paymentMonthId="+absenceDeductionBean.getPaymentMonthId().toString());		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newAbsenceDeductionListForm.htm", method = RequestMethod.GET)
	public ModelAndView absenceDeductionListForm (@ModelAttribute("absenceDeductionForm") AbsenceDeduction absenceDeductionBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		List<PaymentMonth> paymentMonthList = (List<PaymentMonth>)(Object)
				commonService.getAllObjectList("PaymentMonth");		
		
		Collections.reverse(paymentMonthList);
		
		Map <String, Object> model = new HashMap<String, Object>();	
		
		model.put("paymentMonthList", paymentMonthList);	
		return new ModelAndView("absenceDeductionListForm", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/absenceDeductionList.htm", method = RequestMethod.GET)
	public ModelAndView absenceDeductionList (@ModelAttribute("absenceDeductionForm") AbsenceDeduction absenceDeductionBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Company company = (Company) session.getAttribute("company");
		
		List<AbsenceDeduction> absenceDeductionList = (List<AbsenceDeduction>)(Object) 
				commonService.getObjectListByTwoColumn("AbsenceDeduction", "employee.company.id", company.getId().toString(), "paymentMonth.id", absenceDeductionBean.getPaymentMonthId().toString());	
		
		
		Map <String, Object> model = new HashMap<String, Object>();	
		
		model.put("absenceDeductionList", absenceDeductionList);	
		
		return new ModelAndView("absenceDeductionList", model);
	}
	
}
