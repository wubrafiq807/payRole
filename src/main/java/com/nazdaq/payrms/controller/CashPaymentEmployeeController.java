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
import com.nazdaq.payrms.model.CashPaymentEmployee;
import com.nazdaq.payrms.model.Company;
import com.nazdaq.payrms.model.Employee;
import com.nazdaq.payrms.model.PaymentMonth;
import com.nazdaq.payrms.service.CommonService;
import com.nazdaq.payrms.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class CashPaymentEmployeeController implements Constants{

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
	@RequestMapping(value = "/editCashPaymentEmployee.htm", method = RequestMethod.GET)
	public ModelAndView editCashPaymentEmployee(@ModelAttribute("cashPaymentEmployeeForm") CashPaymentEmployee cashPaymentEmployeeBean, Principal principal, HttpSession session,HttpServletRequest request) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Company company = (Company) session.getAttribute("company");
		
		List<PaymentMonth> paymentMonthList = (List<PaymentMonth>)(Object)
				commonService.getAllObjectList("PaymentMonth");		
		
		List<Employee> employeeList = (List<Employee>)(Object)
				commonService.getObjectListByTwoColumn("Employee", "company.id", company.getId().toString(), "status", "1");
		CashPaymentEmployee cashPayment=(CashPaymentEmployee) commonService.getAnObjectByAnyUniqueColumn("CashPaymentEmployee", "id", request.getParameter("id"));
		Collections.reverse(paymentMonthList);
		
		Map <String, Object> model = new HashMap<String, Object>();	
		model.put("edit", true);
		model.put("cashPayment", cashPayment);
		model.put("paymentMonthList", paymentMonthList);	
		model.put("employeeList", employeeList);
		return new ModelAndView("addCashPaymentEmployee", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newCashPaymentEmployeeForm.htm", method = RequestMethod.GET)
	public ModelAndView cashPaymentEmployeeForm (@ModelAttribute("cashPaymentEmployeeForm") CashPaymentEmployee cashPaymentEmployeeBean, Principal principal, HttpSession session) {		
		
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
		return new ModelAndView("addCashPaymentEmployee", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/saveCashPaymentEmployeeOnPage.htm"}, method = RequestMethod.POST)
	private @ResponseBody String saveCashPaymentEmployeeOnPage(@RequestBody String jesonString, Principal principal, HttpSession session) 
			throws JsonGenerationException, JsonMappingException, Exception {
		String toJson = "";
		String response = "";
		Gson gson = new Gson();
		//Company company = (Company) session.getAttribute("company");
		
		CashPaymentEmployee cashPayment = gson.fromJson(jesonString, CashPaymentEmployee.class);
		
		List<CashPaymentEmployee> cashPaymentList = (List<CashPaymentEmployee>)(Object) 
				commonService.getObjectListByTwoColumn("CashPaymentEmployee", "employee.id", cashPayment.getEmployeeId().toString(), "paymentMonth.id", cashPayment.getPaymentMonthId().toString());
		
		if(cashPaymentList.size() > 0) {
			CashPaymentEmployee cashPaymentDb = cashPaymentList.get(0);
			cashPaymentDb.setRemarks(cashPayment.getRemarks());
			cashPaymentDb.setPaymentAmount(cashPayment.getPaymentAmount());
			cashPaymentDb.setModifiedBy(principal.getName());
			cashPaymentDb.setModifiedDate(new Date());
			commonService.saveOrUpdateModelObjectToDB(cashPaymentDb);
			response = "Cash Payment Update Successfully";
		} else {
			Employee employee = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "id", cashPayment.getEmployeeId().toString());
			PaymentMonth paymentMonth = (PaymentMonth) commonService.getAnObjectByAnyUniqueColumn("PaymentMonth", "id", cashPayment.getPaymentMonthId().toString());
			
			CashPaymentEmployee cashPaymentNew = new CashPaymentEmployee();
			cashPaymentNew.setEmployee(employee);
			cashPaymentNew.setPaymentMonth(paymentMonth);	
			
			cashPaymentNew.setPaymentAmount(cashPayment.getPaymentAmount());
			
			cashPaymentNew.setCreatedBy(principal.getName());			
			cashPaymentNew.setCreatedDate(new Date());
			cashPaymentNew.setRemarks(cashPayment.getRemarks());
			commonService.saveOrUpdateModelObjectToDB(cashPaymentNew);
			response = "Cash Payment Save Successfully.";
		}
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		toJson = ow.writeValueAsString(response);
		return toJson;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveCashPaymentEmployee.htm", method = RequestMethod.POST)
	public ModelAndView saveCashPaymentEmployee (@ModelAttribute("cashPaymentEmployeeForm") CashPaymentEmployee cashPaymentEmployeeBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		//Company company = (Company) session.getAttribute("company");
		
		List<CashPaymentEmployee> cashPaymentList = (List<CashPaymentEmployee>)(Object) 
				commonService.getObjectListByTwoColumn("CashPaymentEmployee", "employee.id", cashPaymentEmployeeBean.getEmployeeId().toString(), "paymentMonth.id", cashPaymentEmployeeBean.getPaymentMonthId().toString());	
		
		if(cashPaymentList.size() > 0) {
			CashPaymentEmployee cashPaymentDb = cashPaymentList.get(0);
			cashPaymentDb.setRemarks(cashPaymentEmployeeBean.getRemarks());
			cashPaymentDb.setPaymentAmount(cashPaymentEmployeeBean.getPaymentAmount());
			cashPaymentDb.setModifiedBy(principal.getName());
			cashPaymentDb.setModifiedDate(new Date());
			commonService.saveOrUpdateModelObjectToDB(cashPaymentDb);	
		} else {
			Employee employee = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "id", cashPaymentEmployeeBean.getEmployeeId().toString());
			PaymentMonth paymentMonth = (PaymentMonth) commonService.getAnObjectByAnyUniqueColumn("PaymentMonth", "id", cashPaymentEmployeeBean.getPaymentMonthId().toString());
			CashPaymentEmployee cashPaymentNew = new CashPaymentEmployee();
			cashPaymentNew.setEmployee(employee);
			cashPaymentNew.setPaymentMonth(paymentMonth);	
			
			cashPaymentNew.setPaymentAmount(cashPaymentEmployeeBean.getPaymentAmount());						
			cashPaymentNew.setCreatedBy(principal.getName());			
			cashPaymentNew.setCreatedDate(new Date());
			cashPaymentNew.setRemarks(cashPaymentEmployeeBean.getRemarks());
			commonService.saveOrUpdateModelObjectToDB(cashPaymentNew);			
		}
				
		
		return new ModelAndView("redirect:/cashPaymentEmployeeList.htm?paymentMonthId="+cashPaymentEmployeeBean.getPaymentMonthId().toString());		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newCashPaymentEmployeeListForm.htm", method = RequestMethod.GET)
	public ModelAndView cashPaymentEmployeeListForm (@ModelAttribute("cashPaymentEmployeeForm") CashPaymentEmployee cashPaymentEmployeeBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		//Company company = (Company) session.getAttribute("company");
		
		List<PaymentMonth> paymentMonthList = (List<PaymentMonth>)(Object)
				commonService.getAllObjectList("PaymentMonth");		
		
			Collections.reverse(paymentMonthList);
		
		Map <String, Object> model = new HashMap<String, Object>();	
		
		model.put("paymentMonthList", paymentMonthList);	
		return new ModelAndView("cashPaymentEmployeeListForm", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cashPaymentEmployeeList.htm", method = RequestMethod.GET)
	public ModelAndView advanceList (@ModelAttribute("cashPaymentEmployeeForm") CashPaymentEmployee cashPaymentEmployeeBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Company company = (Company) session.getAttribute("company");
		
		List<CashPaymentEmployee> cashPaymentEmployeeList = (List<CashPaymentEmployee>)(Object) 
				commonService.getObjectListByTwoColumn("CashPaymentEmployee", "employee.company.id", company.getId().toString(), "paymentMonth.id", cashPaymentEmployeeBean.getPaymentMonthId().toString());	
		
		
		Map <String, Object> model = new HashMap<String, Object>();	
		
		model.put("cashPaymentEmployeeList", cashPaymentEmployeeList);	
		
		return new ModelAndView("cashPaymentEmployeeList", model);
	}
	
}
