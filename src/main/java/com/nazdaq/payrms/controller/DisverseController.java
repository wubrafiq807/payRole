package com.nazdaq.payrms.controller;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nazdaq.payrms.model.Disverse;
import com.nazdaq.payrms.model.Employee;
import com.nazdaq.payrms.service.CommonService;
import com.nazdaq.payrms.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class DisverseController implements Constants {

	@Autowired
	private CommonService commonService;

	@Autowired
	private JavaMailSender mailSender;

	@Value("${cc.email.addresss}")
	String ccEmailAddresss;

	@Value("${common.email.address}")
	String commonEmailAddress;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newDisverseForm.htm", method = RequestMethod.GET)
	public ModelAndView DisverseForm(@ModelAttribute("disverseForm") Disverse disverse, Principal principal) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}

		List<Employee> employees = (List<Employee>) (Object) commonService.getAllObjectList("Employee");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("employeeList", employees);
		return new ModelAndView("addDisverse", model);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editDisverse.htm", method = RequestMethod.GET)
	public ModelAndView editDisverse(@ModelAttribute("disverseForm") Disverse disverse, Principal principal) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}

		Disverse disverse1 = (Disverse) commonService.getAnObjectByAnyUniqueColumn("Disverse", "id",
				disverse.getId().toString());
		List<Employee> employees = (List<Employee>) (Object) commonService.getAllObjectList("Employee");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("employeeList", employees);
		model.put("disverse", disverse1);
		return new ModelAndView("addDisverse", model);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getAllDisburse.htm", method = RequestMethod.GET)
	public ModelAndView allDisburseList(Principal principal) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}

	
		Map<String, Object> model = new HashMap<String, Object>();
		List<Disverse> disburseList = (List<Disverse>) (Object) commonService.getAllObjectList("Disverse");
		model.put("disburseList", disburseList);
		return new ModelAndView("disverseList", model);
	}
	

	@RequestMapping(value = "/saveDisverseOrUpdate.htm", method = RequestMethod.POST)
	public ModelAndView saveOrUpdateDisverse(@ModelAttribute("disverseForm") Disverse disverseBean,
			Principal principal) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}

		Map<String, Object> model = new HashMap<String, Object>();

		Employee employee = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "id",
				disverseBean.getEmployeeId().toString());
		if (disverseBean.getId() == null) {
			disverseBean.setCreatedBy(principal.getName());
			disverseBean.setCreatedDate(new Date());
			disverseBean.setDueAmount((disverseBean.getDisverseAmount()
					+ (disverseBean.getDisverseAmount() * disverseBean.getDisverseInterest() / 100)));
			disverseBean.setEmployee(employee);
			disverseBean.setLastDeductFromDue(0.0);

			commonService.saveOrUpdateModelObjectToDB(disverseBean);
		} else {

			Disverse disverseDB = (Disverse) commonService.getAnObjectByAnyUniqueColumn("Disverse", "id",
					disverseBean.getId().toString());
			disverseDB.setDisverseAmount(disverseBean.getDisverseAmount());
			disverseDB.setDisverseInterest(disverseBean.getDisverseInterest());
			disverseDB.setDueAmount((disverseBean.getDisverseAmount()
					+ (disverseBean.getDisverseAmount() * disverseBean.getDisverseInterest() / 100)));
			disverseDB.setModifiedBy(principal.getName());
			disverseDB.setModifiedDate(new Date());
			commonService.saveOrUpdateModelObjectToDB(disverseDB);

		}

		model.put("message", "getAllDisburse.htm");
		return new ModelAndView("success", model);
	}

	@RequestMapping(value = "/deleteDisverse.htm", method = RequestMethod.GET)
	public ModelAndView deleteBranch(Disverse disverseBean, Principal principal) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}
		commonService.deleteAnObjectById("Disverse", disverseBean.getId());
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("message", "newDisverseForm.htm");
		return new ModelAndView("success", model);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/checkDueDisverse.htm", method = RequestMethod.POST)
	public @ResponseBody String checkBranchExists(HttpServletRequest request, HttpServletResponse response,
			Principal principal) throws Exception {
		String text = "true";
		if (request.getParameter("employeeId") != null) {
			
			List<Disverse> diList = (List<Disverse>) (Object) commonService.getObjectListByAnyColumn("Disverse",
					"employee_id", request.getParameter("employeeId"));
			for (Disverse disverse : diList) {
				if (disverse.getDueAmount()>0.0) {
					text = "false";
					break;
				}
			}
		}
		return text;
	}

}