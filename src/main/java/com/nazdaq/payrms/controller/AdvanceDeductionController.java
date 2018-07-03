package com.nazdaq.payrms.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.nazdaq.payrms.model.Advance;
import com.nazdaq.payrms.model.Branch;
import com.nazdaq.payrms.model.Company;
import com.nazdaq.payrms.model.Disverse;
import com.nazdaq.payrms.model.Employee;
import com.nazdaq.payrms.model.PaymentMonth;
import com.nazdaq.payrms.service.CommonService;
import com.nazdaq.payrms.util.Constants;

import net.sf.jasperreports.engine.export.FlashHtmlHandler;

@Controller
@PropertySource("classpath:common.properties")
public class AdvanceDeductionController implements Constants {

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
	@RequestMapping(value = "/editAdvanceDeduct.htm", method = RequestMethod.GET)
	public ModelAndView editAdvanceDeduct(@ModelAttribute("advanceForm") Advance advanceBean, Principal principal,
			HttpSession session, HttpServletRequest request) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}

		Company company = (Company) session.getAttribute("company");

		List<PaymentMonth> paymentMonthList = (List<PaymentMonth>) (Object) commonService
				.getAllObjectList("PaymentMonth");

		List<Employee> employeeList = (List<Employee>) (Object) commonService.getObjectListByTwoColumn("Employee",
				"company.id", company.getId().toString(), "status", "1");
		Advance advance = (Advance) commonService.getAnObjectByAnyUniqueColumn("Advance", "id",
				request.getParameter("id"));
		Collections.reverse(paymentMonthList);

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("edit", true);
		model.put("advance", advance);
		model.put("paymentMonthList", paymentMonthList);
		model.put("employeeList", employeeList);
		return new ModelAndView("addAdvanceForm", model);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newAdvanceForm.htm", method = RequestMethod.GET)
	public ModelAndView advanceForm(@ModelAttribute("advanceForm") Advance advanceBean, Principal principal,
			HttpSession session) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}

		Company company = (Company) session.getAttribute("company");

		List<PaymentMonth> paymentMonthList = (List<PaymentMonth>) (Object) commonService
				.getAllObjectList("PaymentMonth");

		List<Employee> employeeList = (List<Employee>) (Object) commonService.getObjectListByTwoColumn("Employee",
				"company.id", company.getId().toString(), "status", "1");

		Collections.reverse(paymentMonthList);

		Map<String, Object> model = new HashMap<String, Object>();

		model.put("paymentMonthList", paymentMonthList);
		model.put("employeeList", employeeList);
		return new ModelAndView("addAdvanceForm", model);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/saveAdvanceOnPage.htm" }, method = RequestMethod.POST)
	private @ResponseBody String saveAdvanceOnPage(@RequestBody String jesonString, Principal principal,
			HttpSession session, Disverse disverseUpdate)
			throws JsonGenerationException, JsonMappingException, Exception {
		String toJson = "";
		String response = "";
		Gson gson = new Gson();
		// Company company = (Company) session.getAttribute("company");

		Advance advance = gson.fromJson(jesonString, Advance.class);

		List<Advance> advanceList = (List<Advance>) (Object) commonService.getObjectListByTwoColumn("Advance",
				"employee.id", advance.getEmployeeId().toString(), "paymentMonth.id",
				advance.getPaymentMonthId().toString());
		List<Disverse> disverseList = (List<Disverse>) (Object) commonService.getObjectListByAnyColumn("Disverse",
				"employee.id", advance.getEmployeeId().toString());

		disverseList = disverseList.stream().sorted(Comparator.comparing(Disverse::getId).reversed())
				.collect(Collectors.toList());
		
		if (!disverseList.isEmpty()) {
			disverseUpdate = disverseList.get(0);
		}

		if (this.checkpermision(advance.getEmployeeId().toString(), advance.getAdvAmount())) {
			if (advanceList.size() > 0) {
				Advance advanceDb = advanceList.get(0);
				advanceDb.setRemarks(advance.getRemarks());
				advanceDb.setAdvAmount(advance.getAdvAmount());
				advanceDb.setNoOfInstallment(advance.getNoOfInstallment());
				advanceDb.setStatus(advance.getStatus());
				advanceDb.setModifiedBy(principal.getName());
				advanceDb.setModifiedDate(new Date());
				if (disverseUpdate.getLastDeductFromDue() != null) {
					disverseUpdate.setDueAmount((disverseUpdate.getLastDeductFromDue()
							+ disverseUpdate.getDueAmount()) - advance.getAdvAmount());
				} else {
					disverseUpdate.setDueAmount(0.0 + (disverseUpdate.getDueAmount() - advance.getAdvAmount()));
				}
				disverseUpdate.setLastDeductFromDue(advance.getAdvAmount());
				commonService.saveOrUpdateModelObjectToDB(advanceDb);
				response = "Advance Update Successfully";
			} else {
				Employee employee = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "id",
						advance.getEmployeeId().toString());
				PaymentMonth paymentMonth = (PaymentMonth) commonService.getAnObjectByAnyUniqueColumn("PaymentMonth",
						"id", advance.getPaymentMonthId().toString());

				Advance adAdvance = new Advance();
				adAdvance.setEmployee(employee);
				adAdvance.setPaymentMonth(paymentMonth);

				adAdvance.setAdvAmount(advance.getAdvAmount());
				adAdvance.setNoOfInstallment(advance.getNoOfInstallment());
				disverseUpdate.setDueAmount(disverseUpdate.getDueAmount() - advance.getAdvAmount());
				disverseUpdate.setLastDeductFromDue(advance.getAdvAmount());
				adAdvance.setCreatedBy(principal.getName());
				adAdvance.setCreatedDate(new Date());
				adAdvance.setRemarks(advance.getRemarks());
				adAdvance.setStatus(advance.getStatus());
				commonService.saveOrUpdateModelObjectToDB(adAdvance);
				response = "Advance Save Successfully.";
			}
			
			//disburse update
			{
				disverseUpdate.setModifiedBy(principal.getName());
				disverseUpdate.setModifiedDate(new Date());
				commonService.saveOrUpdateModelObjectToDB(disverseUpdate);
			}
		}

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		toJson = ow.writeValueAsString(response);
		return toJson;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveAdvance.htm", method = RequestMethod.POST)
	public ModelAndView saveAdvance(@ModelAttribute("advanceForm") Advance advanceBean, Principal principal,
			HttpSession session, RedirectAttributes redirectAttributes) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}

		// Company company = (Company) session.getAttribute("company");

		List<Advance> advanceList = (List<Advance>) (Object) commonService.getObjectListByTwoColumn("Advance",
				"employee.id", advanceBean.getEmployeeId().toString(), "paymentMonth.id",
				advanceBean.getPaymentMonthId().toString());
		List<Disverse> disverseList = (List<Disverse>) (Object) commonService.getObjectListByAnyColumn("Disverse",
				"employee.id", advanceBean.getEmployeeId().toString());
		disverseList = disverseList.stream().sorted(Comparator.comparing(Disverse::getId).reversed())
				.collect(Collectors.toList());
		Disverse disverseUpdate = new Disverse();
		if (!disverseList.isEmpty()) {
			disverseUpdate = disverseList.get(0);
		}
		if (this.checkpermision(advanceBean.getEmployeeId().toString(), advanceBean.getAdvAmount())) {
			if (advanceList.size() > 0) {
				Advance advanceDb = advanceList.get(0);
				advanceDb.setRemarks(advanceBean.getRemarks());
				advanceDb.setAdvAmount(advanceBean.getAdvAmount());
				advanceDb.setNoOfInstallment(advanceBean.getNoOfInstallment());
				advanceDb.setStatus(advanceBean.getStatus());
				advanceDb.setModifiedBy(principal.getName());
				advanceDb.setModifiedDate(new Date());

				if (disverseUpdate.getLastDeductFromDue() != null) {
					disverseUpdate.setDueAmount((disverseUpdate.getLastDeductFromDue()
							+ disverseUpdate.getDueAmount()) - advanceBean.getAdvAmount());
				} else {
					disverseUpdate.setDueAmount(0.0 + (disverseUpdate.getDueAmount() - advanceBean.getAdvAmount()));
				}

				disverseUpdate.setLastDeductFromDue(advanceBean.getAdvAmount());
				commonService.saveOrUpdateModelObjectToDB(advanceDb);
			} else {
				Employee employee = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "id",
						advanceBean.getEmployeeId().toString());
				PaymentMonth paymentMonth = (PaymentMonth) commonService.getAnObjectByAnyUniqueColumn("PaymentMonth",
						"id", advanceBean.getPaymentMonthId().toString());
				Advance adAdvance = new Advance();
				adAdvance.setEmployee(employee);
				adAdvance.setPaymentMonth(paymentMonth);

				adAdvance.setAdvAmount(advanceBean.getAdvAmount());
				adAdvance.setNoOfInstallment(advanceBean.getNoOfInstallment());
				disverseUpdate.setDueAmount(disverseUpdate.getDueAmount() - advanceBean.getAdvAmount());
				disverseUpdate.setLastDeductFromDue(advanceBean.getAdvAmount());

				adAdvance.setCreatedBy(principal.getName());
				adAdvance.setCreatedDate(new Date());
				adAdvance.setRemarks(advanceBean.getRemarks());
				adAdvance.setStatus(advanceBean.getStatus());
				commonService.saveOrUpdateModelObjectToDB(adAdvance);

			}
			//disburse update
			{
				disverseUpdate.setModifiedBy(principal.getName());
				disverseUpdate.setModifiedDate(new Date());
				commonService.saveOrUpdateModelObjectToDB(disverseUpdate);
			}
		} else {
			redirectAttributes.addFlashAttribute("error", "Advance Deduct Not Apply Due To Permission Error.");
		}

		return new ModelAndView(
				"redirect:/advanceList.htm?paymentMonthId=" + advanceBean.getPaymentMonthId().toString());
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newAdvanceListForm.htm", method = RequestMethod.GET)
	public ModelAndView advanceListForm(@ModelAttribute("advanceForm") Advance advanceBean, Principal principal,
			HttpSession session) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}

		// Company company = (Company) session.getAttribute("company");

		List<PaymentMonth> paymentMonthList = (List<PaymentMonth>) (Object) commonService
				.getAllObjectList("PaymentMonth");

		Collections.reverse(paymentMonthList);

		Map<String, Object> model = new HashMap<String, Object>();

		model.put("paymentMonthList", paymentMonthList);
		return new ModelAndView("advanceListForm", model);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/advanceList.htm", method = RequestMethod.GET)
	public ModelAndView advanceList(@ModelAttribute("advanceForm") Advance advanceBean, Principal principal,
			HttpSession session) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}

		Company company = (Company) session.getAttribute("company");

		List<Advance> advanceList = (List<Advance>) (Object) commonService.getObjectListByTwoColumn("Advance",
				"employee.company.id", company.getId().toString(), "paymentMonth.id",
				advanceBean.getPaymentMonthId().toString());

		Map<String, Object> model = new HashMap<String, Object>();

		model.put("advanceList", advanceList);

		return new ModelAndView("advanceList", model);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/checkAdvanceDeduct.htm", method = RequestMethod.POST)
	public @ResponseBody String checkAdvanceDeduct(HttpServletRequest request, HttpServletResponse response,
			Principal principal) throws Exception {
		String text = "true";
		if (!this.checkpermision(request.getParameter("employeeid"),
				Double.parseDouble(request.getParameter("advAmount")))) {
			text = "false";
		}

		return text;
	}

	@SuppressWarnings("unchecked")
	private boolean checkpermision(String employeeId, Double advAmount) {
		boolean flag = true;
		if (employeeId != null) {
			List<Disverse> disversList = (List<Disverse>) (Object) commonService.getObjectListByAnyColumn("Disverse",
					"employee_id", employeeId);
			disversList = disversList.stream().sorted(Comparator.comparing(Disverse::getId).reversed())
					.collect(Collectors.toList());

			if (!disversList.isEmpty()) {
				Disverse disverseF = disversList.get(0);
				if (disverseF == null) {
					flag = false;
				}
				if (disverseF != null && (advAmount) > disverseF.getDueAmount()) {
					flag = false;
				}

			} else {
				flag = false;
			}

		}
		return flag;
	}
}
