package com.nazdaq.payrms.controller;

import java.security.Principal;
import java.util.Collections;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.nazdaq.payrms.model.Company;
import com.nazdaq.payrms.model.EmployeeSalaryHistory;
import com.nazdaq.payrms.model.PaymentMonth;
import com.nazdaq.payrms.service.CommonService;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Controller
@PropertySource("classpath:common.properties")
public class ReportController {
	
	@Autowired
	private CommonService commonService;

	@Autowired
	private JavaMailSender mailSender;

	@Value("${cc.email.addresss}")
	String ccEmailAddresss;

	@Value("${common.email.address}")
	String commonEmailAddress;	
	
	@Value("${company.logo.basePath}")
	String logoBaseUrl;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/salaryReportForm.htm", method = RequestMethod.GET)
	public ModelAndView salaryReportForm (@ModelAttribute("salaryProcessForm") EmployeeSalaryHistory empSalHistBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		List<PaymentMonth> paymentMonthList = (List<PaymentMonth>)(Object)
				commonService.getAllObjectList("PaymentMonth");		
		
			Collections.reverse(paymentMonthList);
		
		Map <String, Object> model = new HashMap<String, Object>();	
		
		model.put("paymentMonthList", paymentMonthList);	
		return new ModelAndView("salaryReportForm", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST, value = "/salaryReportPdf")
	public ModelAndView generaterevenureport(@ModelAttribute("salaryProcessForm") EmployeeSalaryHistory empSalHistBean, HttpServletRequest request,
			ModelAndView modelAndView, Principal principal, HttpSession session) {
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Company company = (Company) session.getAttribute("company");		
		
		// process start from here
		PaymentMonth payMonth = (PaymentMonth) commonService.getAnObjectByAnyUniqueColumn("PaymentMonth", "id", request.getParameter("paymentMonthId").toString());
		
		List<EmployeeSalaryHistory> salaryList = (List<EmployeeSalaryHistory>)(Object)
				commonService.getObjectListByTwoColumn("EmployeeSalaryHistory", "employee.company.id", company.getId().toString(), "paymentMonth.id", payMonth.getId().toString());
		
		List<PaymentMonth> paymentMonthList = (List<PaymentMonth>)(Object)
				commonService.getAllObjectList("PaymentMonth");		
		
			Collections.reverse(paymentMonthList);
		
		String reportName = "salaryReportPdf";		
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		
		JRDataSource JRdataSource = new JRBeanCollectionDataSource(salaryList);

		parameterMap.put("datasource", JRdataSource);
		//params
		parameterMap.put("P_COMPANY_NAME", company.getName());
		parameterMap.put("P_COMPANY_ADDRESS", company.getAddress());
		parameterMap.put("P_PHONE_NO", "Phone: "+company.getPhone());
		parameterMap.put("P_EMAIL_ADDRESS", "Email: "+company.getEmail());
		parameterMap.put("P_WEBSITE", "Website: "+company.getWebsite());
		parameterMap.put("P_REPORT_TITLE", "Allowance Report Month of " + payMonth.getPaymentMonthName()+"-"+payMonth.getPayYear());
		parameterMap.put("P_LOGO_URL", logoBaseUrl+company.getCompanyKeyword()+".jpg");

		// pdfReport bean has ben declared in the jasper-views.xml file
		modelAndView = new ModelAndView(reportName, parameterMap);

		return modelAndView;
	}

}
