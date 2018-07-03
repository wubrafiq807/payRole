package com.nazdaq.payrms.controller;

import java.security.Principal;
import java.util.Collections;
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

import com.nazdaq.payrms.model.PaymentMonth;
import com.nazdaq.payrms.service.CommonService;
import com.nazdaq.payrms.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class PaymentMonthController implements Constants{

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${cc.email.addresss}")
	String ccEmailAddresss;
	
	@Value("${common.email.address}")
	String commonEmailAddress;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newPaymentMonthForm.htm", method = RequestMethod.GET)
	public ModelAndView paymentMonthForm (@ModelAttribute("paymentMonthForm") PaymentMonth paymentMonthBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		List<PaymentMonth> paymentMonthList = (List<PaymentMonth>)(Object)
				commonService.getAllObjectList("PaymentMonth");		
		Collections.reverse(paymentMonthList);
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("paymentMonthList", paymentMonthList);		
		return new ModelAndView("addPaymentMonth", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editPaymentMonth.htm", method = RequestMethod.GET)
	public ModelAndView editPaymentMonth (@ModelAttribute("paymentMonthForm") PaymentMonth paymentMonthBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		List<PaymentMonth> paymentMonthList = (List<PaymentMonth>)(Object)
				commonService.getAllObjectList("PaymentMonth");	
		PaymentMonth paymentMonth = (PaymentMonth)commonService.getAnObjectByAnyUniqueColumn("PaymentMonth", "id", paymentMonthBean.getId().toString());
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("paymentMonthList", paymentMonthList);
		model.put("edit", true);
		model.put("paymentMonth", paymentMonth);
		return new ModelAndView("addPaymentMonth", model);		
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/savePaymentMonth.htm", method = RequestMethod.POST)
	public ModelAndView saveOrUpdatePaymentMonth (@ModelAttribute("paymentMonthForm") PaymentMonth paymentMonthBean, Principal principal) {	
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Map <String, Object> model = new HashMap<String, Object>();	
		
		if(paymentMonthBean.getId() != null) {
			PaymentMonth paymentMonth = (PaymentMonth)commonService.getAnObjectByAnyUniqueColumn("PaymentMonth", "id", paymentMonthBean.getId().toString());
			List<PaymentMonth> paymentMonthList = (List<PaymentMonth>)(Object)
					commonService.getObjectListByTwoColumn("PaymentMonth", "payYear", paymentMonthBean.getPayYear(), "paymentMonthName", paymentMonthBean.getPaymentMonthName());
			 //PaymentMonth paymentMonthByName = commonService.getAnObjectByAnyUniqueColumn("PaymentMonth", "paymentMonthName", paymentMonthBean.getName().toString());
			if(paymentMonthList.size() > 0) {
				if(!paymentMonthList.get(0).getId().toString().equals(paymentMonth.getId().toString())) {
					model.put("message", "newPaymentMonthForm.htm");
					return new ModelAndView("failed", model);
				}
				
			}else {
				paymentMonth.setPayName(paymentMonthBean.getPayName());
				commonService.saveOrUpdateModelObjectToDB(paymentMonth);
			}
		} else {
			String payName = paymentMonthBean.getPaymentMonthName()+"_"+paymentMonthBean.getPayYear();
			paymentMonthBean.setPayName(payName);			
			PaymentMonth paymentMonth = (PaymentMonth)commonService.getAnObjectByAnyUniqueColumn("PaymentMonth", "payName", paymentMonthBean.getPayName().toString());
			if(paymentMonth != null) {
				model.put("message", "newPaymentMonthForm.htm");
				return new ModelAndView("failed", model);
			} else {
				commonService.saveOrUpdateModelObjectToDB(paymentMonthBean);
			}
		}
		
		
		model.put("message", "newPaymentMonthForm.htm");
		return new ModelAndView("success", model);
	}
	
	@RequestMapping(value = "/deletePaymentMonth.htm", method = RequestMethod.GET)
	public ModelAndView deletePaymentMonth (PaymentMonth paymentMonthBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		commonService.deleteAnObjectById("PaymentMonth", paymentMonthBean.getId());
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("message", "newPaymentMonthForm.htm");
		return new ModelAndView("success", model);		
	}	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/checkPaymentMonth.htm", method = RequestMethod.POST)
	 public @ResponseBody String checkPaymentMonthExists(HttpServletRequest request, HttpServletResponse response, Principal principal)throws Exception {
		String text = "true";		
		if(request.getParameter("paymentMonthName") != null){
			List<PaymentMonth> paymentMonthList = (List<PaymentMonth>)(Object)
					commonService.getObjectListByTwoColumn("PaymentMonth", "payYear", request.getParameter("payYear"), "paymentMonthName",request.getParameter("paymentMonthName"));
		//	PaymentMonth paymentMonth = (PaymentMonth)commonService.getAnObjectByAnyUniqueColumn("PaymentMonth", "payName", request.getParameter("payName").toString());
			if(!paymentMonthList.isEmpty()){ 
				text = "false";
			}
		}	 
		return text;
	 }
	
}