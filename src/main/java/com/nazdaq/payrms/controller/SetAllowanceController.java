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
import com.nazdaq.payrms.model.AllowanceSetup;
import com.nazdaq.payrms.service.CommonService;
import com.nazdaq.payrms.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class SetAllowanceController implements Constants{
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${cc.email.addresss}")
	String ccEmailAddresss;
	
	@Value("${common.email.address}")
	String commonEmailAddress;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newAllowanceSetupForm.htm", method = RequestMethod.GET)
	public ModelAndView allowanceSetupForm (@ModelAttribute("allowanceSetupForm") AllowanceSetup allowanceSetupBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Company company = (Company) session.getAttribute("company");
		
		List<AllowanceSetup> allowanceSetupList = (List<AllowanceSetup>)(Object)commonService.getObjectListByAnyColumn("AllowanceSetup", "company.id", company.getId().toString());
		
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("allowanceSetupList", allowanceSetupList);		
		return new ModelAndView("addAllowanceSetup", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editAllowanceSetup.htm", method = RequestMethod.GET)
	public ModelAndView editAllowanceSetup (@ModelAttribute("allowanceSetupForm") AllowanceSetup allowanceSetupBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Company company = (Company) session.getAttribute("company");
		
		List<AllowanceSetup> allowanceSetupList = (List<AllowanceSetup>)(Object)commonService.getObjectListByAnyColumn("AllowanceSetup", "company.id", company.getId().toString());
		
		AllowanceSetup allowanceSetup = (AllowanceSetup)commonService.getAnObjectByAnyUniqueColumn("AllowanceSetup", "id", allowanceSetupBean.getId().toString());
		
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("allowanceSetupList", allowanceSetupList);
		model.put("edit", true);
		model.put("allowanceSetup", allowanceSetup);
		return new ModelAndView("addAllowanceSetup", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveAllowanceSetup.htm", method = RequestMethod.POST)
	public ModelAndView saveOrUpdateAllowanceSetup (@ModelAttribute("allowanceSetupForm") AllowanceSetup allowanceSetupBean, Principal principal, HttpSession session) {	
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Map <String, Object> model = new HashMap<String, Object>();	
		Company company = (Company) session.getAttribute("company");
		
		if(allowanceSetupBean.getId() != null) {
			
			AllowanceSetup allowanceSetup = (AllowanceSetup)commonService.getAnObjectByAnyUniqueColumn("AllowanceSetup", "id", allowanceSetupBean.getId().toString());
			allowanceSetupBean.setCompany(allowanceSetup.getCompany());
			List<AllowanceSetup> allowanceSetupList = (List<AllowanceSetup>)(Object) 
					commonService.getObjectListByTwoColumn("AllowanceSetup", "company.id", company.getId().toString(), "name", allowanceSetupBean.getName().toString());
			
			if(allowanceSetupList.size() > 0) {
				AllowanceSetup allowanceSetupByName = allowanceSetupList.get(0);
				if(!allowanceSetupByName.getId().toString().equals(allowanceSetup.getId().toString())) {
					model.put("message", "newAllowanceSetupForm.htm");
					return new ModelAndView("failed", model);
				}else {
				//	allowanceSetupByName.setName(allowanceSetupBean.getName());
					commonService.saveOrUpdateModelObjectToDB(allowanceSetupBean);
				}
				
			}else {
				//allowanceSetup.setName(allowanceSetupBean.getName());
				
				commonService.saveOrUpdateModelObjectToDB(allowanceSetupBean);
			}
		} else {
			
			List<AllowanceSetup> allowanceSetupList = (List<AllowanceSetup>)(Object) 
					commonService.getObjectListByTwoColumn("AllowanceSetup", "company.id", company.getId().toString(), "name", allowanceSetupBean.getName().toString());
			
			if(allowanceSetupList.size() > 0) {
				model.put("message", "newAllowanceSetupForm.htm");
				return new ModelAndView("failed", model);
			} else {
				
				allowanceSetupBean.setCompany(company);
				
				commonService.saveOrUpdateModelObjectToDB(allowanceSetupBean);
			}
		}
		
		
		model.put("message", "newAllowanceSetupForm.htm");
		return new ModelAndView("success", model);
	}
	
	@RequestMapping(value = "/deleteAllowanceSetup.htm", method = RequestMethod.GET)
	public ModelAndView deleteAllowanceSetup (AllowanceSetup allowanceSetupBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		commonService.deleteAnObjectById("AllowanceSetup", allowanceSetupBean.getId());
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("message", "newAllowanceSetupForm.htm");
		return new ModelAndView("success", model);		
	}	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/checkAllowanceSetup.htm", method = RequestMethod.POST)
	 public @ResponseBody String checkAllowanceSetupExists(HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session)throws Exception {
		Company company = (Company) session.getAttribute("company");	
		String text = "true";		
		if(request.getParameter("name") != null){
			List<AllowanceSetup> allowanceSetupList = (List<AllowanceSetup>)(Object) 
					commonService.getObjectListByTwoColumn("AllowanceSetup", "company.id", company.getId().toString(), "name", request.getParameter("name").toString());
			if(allowanceSetupList.size() > 0){ 
				text = "false";
			}
		}	 
		return text;
	 }
	
}
