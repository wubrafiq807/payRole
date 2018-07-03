package com.nazdaq.payrms.controller;

import java.security.Principal;
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
import com.nazdaq.payrms.model.FixedIncomeTax;
import com.nazdaq.payrms.model.Company;
import com.nazdaq.payrms.model.Employee;
import com.nazdaq.payrms.service.CommonService;
import com.nazdaq.payrms.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class FixedIncomeTaxController implements Constants{

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
	@RequestMapping(value = "/editFixedIncomeTax.htm", method = RequestMethod.GET)
	public ModelAndView editFixedIncomeTax(@ModelAttribute("fixedIncomeTaxForm") FixedIncomeTax fixedIncomeTaxBean, Principal principal, HttpSession session,HttpServletRequest request) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Company company = (Company) session.getAttribute("company");
		
		List<Employee> employeeList = (List<Employee>)(Object)
				commonService.getObjectListByTwoColumn("Employee", "company.id", company.getId().toString(), "status", "1");
		FixedIncomeTax fixedIncomeTax=(FixedIncomeTax) commonService.getAnObjectByAnyUniqueColumn("FixedIncomeTax", "id", request.getParameter("id"));
		
		Map <String, Object> model = new HashMap<String, Object>();	
		model.put("edit", true);
		model.put("fixedIncomeTax", fixedIncomeTax);	
		model.put("employeeList", employeeList);
		return new ModelAndView("addFixedIncomeTax", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newFixedIncomeTaxForm.htm", method = RequestMethod.GET)
	public ModelAndView fixedIncomeTaxForm (@ModelAttribute("fixedIncomeTaxForm") FixedIncomeTax fixedIncomeTaxBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Company company = (Company) session.getAttribute("company");
				
		List<Employee> employeeList = (List<Employee>)(Object)
				commonService.getObjectListByTwoColumn("Employee", "company.id", company.getId().toString(), "status", "1");		
		
		Map <String, Object> model = new HashMap<String, Object>();	
		
		model.put("employeeList", employeeList);
		return new ModelAndView("addFixedIncomeTax", model);		
	}
	
	@RequestMapping(value = {"/saveFixedIncomeTaxOnPage.htm"}, method = RequestMethod.POST)
	private @ResponseBody String saveFixedIncomeTaxOnPage(@RequestBody String jesonString, Principal principal, HttpSession session) 
			throws JsonGenerationException, JsonMappingException, Exception {
		String toJson = "";
		String response = "";
		Gson gson = new Gson();
		
		FixedIncomeTax fixedIncomeTax = gson.fromJson(jesonString, FixedIncomeTax.class);
		
				
		FixedIncomeTax fixedIncomeTaxDB = (FixedIncomeTax) commonService.getAnObjectByAnyUniqueColumn("FixedIncomeTax", "employee.id", fixedIncomeTax.getEmployeeId().toString());
		
		if(fixedIncomeTaxDB != null) {
			fixedIncomeTaxDB.setRemarks(fixedIncomeTax.getRemarks());
			fixedIncomeTaxDB.setTaxAmount(fixedIncomeTax.getTaxAmount());
			fixedIncomeTaxDB.setModifiedBy(principal.getName());
			fixedIncomeTaxDB.setModifiedDate(new Date());
			commonService.saveOrUpdateModelObjectToDB(fixedIncomeTaxDB);
			response = "Income Tax Update Successfully";
		} else {
			Employee employee = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "id", fixedIncomeTax.getEmployeeId().toString());
			
			FixedIncomeTax fixedIncomeTaxNew = new FixedIncomeTax();
			fixedIncomeTaxNew.setEmployee(employee);
			
			fixedIncomeTaxNew.setTaxAmount(fixedIncomeTax.getTaxAmount());
			
			fixedIncomeTaxNew.setCreatedBy(principal.getName());			
			fixedIncomeTaxNew.setCreatedDate(new Date());
			fixedIncomeTaxNew.setRemarks(fixedIncomeTax.getRemarks());
			commonService.saveOrUpdateModelObjectToDB(fixedIncomeTaxNew);
			response = "Income Tax Save Successfully.";
		}
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		toJson = ow.writeValueAsString(response);
		return toJson;
	}
	
	@RequestMapping(value = "/saveFixedIncomeTax.htm", method = RequestMethod.POST)
	public ModelAndView saveFixedIncomeTax (@ModelAttribute("fixedIncomeTaxForm") FixedIncomeTax fixedIncomeTaxBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		FixedIncomeTax fixedIncomeTaxDB = (FixedIncomeTax) commonService.getAnObjectByAnyUniqueColumn("FixedIncomeTax", "employee.id", fixedIncomeTaxBean.getEmployeeId().toString());
		
		if(fixedIncomeTaxDB != null) {
			fixedIncomeTaxDB.setRemarks(fixedIncomeTaxBean.getRemarks());
			fixedIncomeTaxDB.setTaxAmount(fixedIncomeTaxBean.getTaxAmount());
			fixedIncomeTaxDB.setModifiedBy(principal.getName());
			fixedIncomeTaxDB.setModifiedDate(new Date());
			commonService.saveOrUpdateModelObjectToDB(fixedIncomeTaxDB);
		} else {
			Employee employee = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "id", fixedIncomeTaxBean.getEmployeeId().toString());
			FixedIncomeTax fixedIncomeTaxNew = new FixedIncomeTax();
			fixedIncomeTaxNew.setEmployee(employee);
						
			fixedIncomeTaxNew.setTaxAmount(fixedIncomeTaxBean.getTaxAmount());				
			fixedIncomeTaxNew.setCreatedBy(principal.getName());			
			fixedIncomeTaxNew.setCreatedDate(new Date());
			fixedIncomeTaxNew.setRemarks(fixedIncomeTaxBean.getRemarks());
			commonService.saveOrUpdateModelObjectToDB(fixedIncomeTaxNew);			
		}
				
		
		return new ModelAndView("redirect:/fixedIncomeTaxList.htm");		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/fixedIncomeTaxList.htm", method = RequestMethod.GET)
	public ModelAndView fixedIncomeTaxList (FixedIncomeTax fixedIncomeTaxBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Company company = (Company) session.getAttribute("company");
		
		 
		List<FixedIncomeTax> fixedIncomeTaxList = (List<FixedIncomeTax>)(Object) 
				commonService.getObjectListByAnyColumn("FixedIncomeTax", "employee.company.id", company.getId().toString());
		
		Map <String, Object> model = new HashMap<String, Object>();	
		
		model.put("fixedIncomeTaxList", fixedIncomeTaxList);	
		
		return new ModelAndView("fixedIncomeTaxList", model);
	}
	
}
