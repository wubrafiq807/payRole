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
import com.nazdaq.payrms.model.Company;
import com.nazdaq.payrms.model.Employee;
import com.nazdaq.payrms.model.Salary;
import com.nazdaq.payrms.model.SalaryIncrementHist;
import com.nazdaq.payrms.service.CommonService;
import com.nazdaq.payrms.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class SalaryController implements Constants {

	@Autowired
	private CommonService commonService;

	@Autowired
	private JavaMailSender mailSender;

	@Value("${cc.email.addresss}")
	String ccEmailAddresss;

	@Value("${common.email.address}")
	String commonEmailAddress;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editSalary.htm", method = RequestMethod.GET)
	public ModelAndView editSalary(@ModelAttribute("salaryForm") Salary salaryBean, Principal principal,
			HttpSession session, HttpServletRequest request) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}

		Company company = (Company) session.getAttribute("company");

		List<Employee> employeeList = (List<Employee>) (Object) commonService.getObjectListByTwoColumn("Employee",
				"company.id", company.getId().toString(), "status", "1");

		Salary salary = (Salary) commonService.getAnObjectByAnyUniqueColumn("Salary", "id", request.getParameter("id"));

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("edit", true);
		model.put("salary", salary);
		model.put("employeeList", employeeList);
		return new ModelAndView("addSalary", model);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newSalaryForm.htm", method = RequestMethod.GET)
	public ModelAndView newSalaryForm(@ModelAttribute("salaryForm") Salary salaryBean, Principal principal,
			HttpSession session) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}

		Company company = (Company) session.getAttribute("company");

		List<Employee> employeeList = (List<Employee>) (Object) commonService.getObjectListByTwoColumn("Employee",
				"company.id", company.getId().toString(), "status", "1");

		Map<String, Object> model = new HashMap<String, Object>();

		model.put("employeeList", employeeList);
		return new ModelAndView("addSalary", model);
	}

	@RequestMapping(value = { "/saveSalaryOnPage.htm" }, method = RequestMethod.POST)
	private @ResponseBody String saveSalaryOnPage(@RequestBody String jesonString, Principal principal,
			HttpSession session) throws JsonGenerationException, JsonMappingException, Exception {
		String toJson = "";
		String response = "";
		Gson gson = new Gson();

		Salary salary = gson.fromJson(jesonString, Salary.class);

		Salary salaryDB = (Salary) commonService.getAnObjectByAnyUniqueColumn("Salary", "employee.id",
				salary.getEmployeeId().toString());
		
		Employee employee = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "id",
				salary.getEmployeeId().toString());
		
		if (salaryDB == null || (salaryDB != null && (salary.getGrossSalary() != salaryDB.getGrossSalary()) || salary.getConsalitedSalary() != salaryDB.getConsalitedSalary() )) {
			this.addSalaryHistoryToDB(salary, employee, principal);
		}
		
		if (salaryDB != null) {
			salaryDB.setRemarks(salary.getRemarks());
			
			if (salary.getGrossSalary() != salaryDB.getGrossSalary() || salary.getConsalitedSalary() != salaryDB.getConsalitedSalary()) {
				if(salary.getConsalitedSalary() > 0) {
					salaryDB.setLastReviewAmt(salary.getConsalitedSalary() - salaryDB.getConsalitedSalary());
				} else {
					salaryDB.setLastReviewAmt(salary.getGrossSalary() - salaryDB.getGrossSalary());
				}
				
			}

			salaryDB.setGrossSalary(salary.getGrossSalary());
			salaryDB.setBasicSalary(salary.getBasicSalary());
			salaryDB.setConsalitedSalary(salary.getConsalitedSalary());
			salaryDB.setOtherSalary(salary.getOtherSalary());
			salaryDB.setMedical(salary.getMedical());
			salaryDB.setConveyance(salary.getConveyance());
			salaryDB.setHouseRent(salary.getHouseRent());

			salaryDB.setModifiedBy(principal.getName());
			salaryDB.setModifiedDate(new Date());
			commonService.saveOrUpdateModelObjectToDB(salaryDB);
			response = "Salary Update Successfully";
		} else {

			Salary salaryNew = new Salary();
			salaryNew.setEmployee(employee);

			salaryNew.setGrossSalary(salary.getGrossSalary());
			salaryNew.setBasicSalary(salary.getBasicSalary());
			salaryNew.setConsalitedSalary(salary.getConsalitedSalary());
			salaryNew.setOtherSalary(salary.getOtherSalary());
			salaryNew.setMedical(salary.getMedical());
			salaryNew.setConveyance(salary.getConveyance());
			salaryNew.setHouseRent(salary.getHouseRent());

			salaryNew.setCreatedBy(principal.getName());
			salaryNew.setCreatedDate(new Date());
			salaryNew.setRemarks(salary.getRemarks());
			commonService.saveOrUpdateModelObjectToDB(salaryNew);
			response = "Salary Save Successfully.";
		}

		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		toJson = ow.writeValueAsString(response);
		return toJson;
	}

	private int addSalaryHistoryToDB(Salary salary, Employee employee, Principal principal) {
		SalaryIncrementHist salaryIncrementHist = new SalaryIncrementHist();
		salaryIncrementHist.setBasicSalary(salary.getBasicSalary());
		salaryIncrementHist.setGrossSalary(salary.getGrossSalary());
		salaryIncrementHist.setConsalitedSalary(salary.getConsalitedSalary());
		salaryIncrementHist.setConveyance(salary.getConveyance());
		salaryIncrementHist.setEmployee(employee);
		salaryIncrementHist.setHouseRent(salary.getHouseRent());
		salaryIncrementHist.setMedical(salary.getMedical());
		salaryIncrementHist.setOtherSalary(salary.getOtherSalary());
		salaryIncrementHist.setCreatedBy(principal.getName());
		salaryIncrementHist.setCreatedDate(new Date());
		return commonService.saveWithReturnId(salaryIncrementHist);
	}

	@RequestMapping(value = "/saveSalary.htm", method = RequestMethod.POST)
	public ModelAndView saveSalary(@ModelAttribute("salaryForm") Salary salaryBean, Principal principal,
			HttpSession session) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}

		Salary salaryDB = (Salary) commonService.getAnObjectByAnyUniqueColumn("Salary", "employee.id",
				salaryBean.getEmployeeId().toString());
		Employee employee = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "id",
				salaryBean.getEmployeeId().toString());
		
		if (salaryDB == null || (salaryDB != null && (salaryBean.getGrossSalary() != salaryDB.getGrossSalary() || salaryBean.getConsalitedSalary() != salaryDB.getConsalitedSalary() ))) {
			this.addSalaryHistoryToDB(salaryBean, employee, principal);
		}
		
		if (salaryDB != null) {
			
			if (salaryBean.getGrossSalary() != salaryDB.getGrossSalary() || salaryBean.getConsalitedSalary() != salaryDB.getConsalitedSalary()) {
				if(salaryBean.getConsalitedSalary() > 0) {
					salaryDB.setLastReviewAmt(salaryBean.getConsalitedSalary() - salaryDB.getConsalitedSalary());
				} else {
					salaryDB.setLastReviewAmt(salaryBean.getGrossSalary() - salaryDB.getGrossSalary());
				}
				
			}
			
			salaryDB.setRemarks(salaryBean.getRemarks());

			salaryDB.setGrossSalary(salaryBean.getGrossSalary());
			salaryDB.setBasicSalary(salaryBean.getBasicSalary());
			salaryDB.setConsalitedSalary(salaryBean.getConsalitedSalary());
			salaryDB.setOtherSalary(salaryBean.getOtherSalary());
			salaryDB.setMedical(salaryBean.getMedical());
			salaryDB.setConveyance(salaryBean.getConveyance());
			salaryDB.setHouseRent(salaryBean.getHouseRent());

			salaryDB.setModifiedBy(principal.getName());
			salaryDB.setModifiedDate(new Date());
			
			commonService.saveOrUpdateModelObjectToDB(salaryDB);
		} else {

			Salary salaryNew = new Salary();
			salaryNew.setEmployee(employee);

			salaryNew.setGrossSalary(salaryBean.getGrossSalary());
			salaryNew.setBasicSalary(salaryBean.getBasicSalary());
			salaryNew.setConsalitedSalary(salaryBean.getConsalitedSalary());
			salaryNew.setOtherSalary(salaryBean.getOtherSalary());
			salaryNew.setMedical(salaryBean.getMedical());
			salaryNew.setHouseRent(salaryBean.getHouseRent());
			salaryNew.setConveyance(salaryBean.getConveyance());
			salaryNew.setCreatedBy(principal.getName());
			salaryNew.setCreatedDate(new Date());
			salaryNew.setRemarks(salaryBean.getRemarks());
			commonService.saveOrUpdateModelObjectToDB(salaryNew);
		}
		
		return new ModelAndView("redirect:/salaryList.htm");
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/salaryList.htm", method = RequestMethod.GET)
	public ModelAndView salaryList(Salary salaryBean, Principal principal, HttpSession session) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}

		Company company = (Company) session.getAttribute("company");

		List<Salary> salaryList = (List<Salary>) (Object) commonService.getObjectListByAnyColumn("Salary",
				"employee.company.id", company.getId().toString());

		Map<String, Object> model = new HashMap<String, Object>();

		model.put("salaryList", salaryList);

		return new ModelAndView("salaryList", model);
	}

}
