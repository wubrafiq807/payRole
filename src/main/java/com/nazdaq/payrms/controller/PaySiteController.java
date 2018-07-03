package com.nazdaq.payrms.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

import com.nazdaq.payrms.model.Company;

import com.nazdaq.payrms.model.PaySite;
import com.nazdaq.payrms.service.CommonService;
import com.nazdaq.payrms.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class PaySiteController implements Constants{

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${cc.email.addresss}")
	String ccEmailAddresss;
	
	@Value("${common.email.address}")
	String commonEmailAddress;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newPaySiteForm.htm", method = RequestMethod.GET)
	public ModelAndView paySiteForm (@ModelAttribute("paySiteForm") PaySite paySiteBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Company company = (Company) session.getAttribute("company");
		List<PaySite> paySiteList = (List<PaySite>)(Object)commonService.getObjectListByAnyColumn("PaySite", "company.id", company.getId().toString());
		
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("paySiteList", paySiteList);		
		return new ModelAndView("addPaySite", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editPaySite.htm", method = RequestMethod.GET)
	public ModelAndView editPaySite (@ModelAttribute("paySiteForm") PaySite paySiteBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Company company = (Company) session.getAttribute("company");
		
		List<PaySite> paySiteList = (List<PaySite>)(Object)commonService.getObjectListByAnyColumn("PaySite", "company.id", company.getId().toString());
		
		PaySite paySite = (PaySite)commonService.getAnObjectByAnyUniqueColumn("PaySite", "id", paySiteBean.getId().toString());
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("paySiteList", paySiteList);
		model.put("edit", true);
		model.put("paySite", paySite);
		return new ModelAndView("addPaySite", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/savePaySite.htm", method = RequestMethod.POST)
	public ModelAndView saveOrUpdatePaySite (@ModelAttribute("paySiteForm") PaySite paySiteBean, Principal principal, HttpSession session) {	
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Map <String, Object> model = new HashMap<String, Object>();	
		Company company = (Company) session.getAttribute("company");
		
		if(paySiteBean.getId() != null) {
			PaySite paySite = (PaySite)commonService.getAnObjectByAnyUniqueColumn("PaySite", "id", paySiteBean.getId().toString());
			
			List<PaySite> paySiteList = (List<PaySite>)(Object) commonService.getObjectListByTwoColumn("PaySite", "company.id", company.getId().toString(), "name", paySiteBean.getName().toString());
			
			if(paySiteList.size() > 0) {
				PaySite paySiteByName = paySiteList.get(0);
				if(!paySiteByName.getId().toString().equals(paySite.getId().toString())) {
					model.put("message", "newPaySiteForm.htm");
					return new ModelAndView("failed", model);
				}else {
					paySiteByName.setName(paySiteBean.getName());
					commonService.saveOrUpdateModelObjectToDB(paySiteByName);
				}
				
			}else {
				paySite.setName(paySiteBean.getName());
				commonService.saveOrUpdateModelObjectToDB(paySite);
			}
		} else {
			List<PaySite> holidayList = (List<PaySite>)(Object) commonService.getObjectListByTwoColumn("PaySite", "company.id", company.getId().toString(), "name", paySiteBean.getName().toString());
			if(holidayList.size() > 0) {
				model.put("message", "newPaySiteForm.htm");
				return new ModelAndView("failed", model);
			} else {
				paySiteBean.setCompany(company);
				commonService.saveOrUpdateModelObjectToDB(paySiteBean);
			}
		}
		
		
		model.put("message", "newPaySiteForm.htm");
		return new ModelAndView("success", model);
	}
	
	@RequestMapping(value = "/deletePaySite.htm", method = RequestMethod.GET)
	public ModelAndView deletePaySite (PaySite paySiteBean, Principal principal,HttpServletRequest request) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		commonService.deleteAnObjectById("PaySite", Integer.parseInt(request.getParameter("id")));
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("message", "newPaySiteForm.htm");
		return new ModelAndView("success", model);		
	}	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/checkPaySite.htm", method = RequestMethod.POST)
	 public @ResponseBody String checkPaySiteExists(HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session)throws Exception {
		Company company = (Company) session.getAttribute("company");	
		String text = "true";		
		if(request.getParameter("name") != null){
			List<PaySite> paySiteList = (List<PaySite>)(Object)commonService.getObjectListByTwoColumn("PaySite", "company.id", company.getId().toString(), "name", request.getParameter("name").toString());
			if(paySiteList.size() > 0){ 
				text = "false";
			}
		}	 
		return text;
	 }
	
}