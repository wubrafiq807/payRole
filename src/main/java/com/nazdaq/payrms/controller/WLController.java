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
import com.nazdaq.payrms.model.WorkLocation;
import com.nazdaq.payrms.service.CommonService;
import com.nazdaq.payrms.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class WLController implements Constants{

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${cc.email.addresss}")
	String ccEmailAddresss;
	
	@Value("${common.email.address}")
	String commonEmailAddress;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newWorkLocationForm.htm", method = RequestMethod.GET)
	public ModelAndView workLocationForm (@ModelAttribute("workLocationForm") WorkLocation workLocationBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Company company = (Company) session.getAttribute("company");
		
		List<WorkLocation> workLocationList = (List<WorkLocation>)(Object)commonService.getObjectListByAnyColumn("WorkLocation", "company.id", company.getId().toString());
		
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("workLocationList", workLocationList);		
		return new ModelAndView("addWorkLocation", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editWorkLocation.htm", method = RequestMethod.GET)
	public ModelAndView editWorkLocation (@ModelAttribute("workLocationForm") WorkLocation workLocationBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Company company = (Company) session.getAttribute("company");
		
		List<WorkLocation> workLocationList = (List<WorkLocation>)(Object)commonService.getObjectListByAnyColumn("WorkLocation", "company.id", company.getId().toString());
		
		WorkLocation workLocation = (WorkLocation)commonService.getAnObjectByAnyUniqueColumn("WorkLocation", "id", workLocationBean.getId().toString());
		
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("workLocationList", workLocationList);
		model.put("edit", true);
		model.put("workLocation", workLocation);
		return new ModelAndView("addWorkLocation", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveWorkLocation.htm", method = RequestMethod.POST)
	public ModelAndView saveOrUpdateWorkLocation (@ModelAttribute("workLocationForm") WorkLocation workLocationBean, Principal principal, HttpSession session) {	
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Map <String, Object> model = new HashMap<String, Object>();	
		Company company = (Company) session.getAttribute("company");
		
		if(workLocationBean.getId() != null) {
			WorkLocation workLocation = (WorkLocation)commonService.getAnObjectByAnyUniqueColumn("WorkLocation", "id", workLocationBean.getId().toString());
			List<WorkLocation> workLocationList = (List<WorkLocation>)(Object) commonService.getObjectListByTwoColumn("WorkLocation", "company.id", company.getId().toString(), "name", workLocationBean.getName().toString());
			
			if(workLocationList.size() > 0) {
				WorkLocation workLocationByName = workLocationList.get(0);
				if(!workLocationByName.getId().toString().equals(workLocation.getId().toString())) {
					model.put("message", "newWorkLocationForm.htm");
					return new ModelAndView("failed", model);
				}else {
					workLocationByName.setName(workLocationBean.getName());
					commonService.saveOrUpdateModelObjectToDB(workLocationByName);
				}
				
			}else {
				workLocation.setName(workLocationBean.getName());
				commonService.saveOrUpdateModelObjectToDB(workLocation);
			}
		} else {
			List<WorkLocation> workLocationList = (List<WorkLocation>)(Object) commonService.getObjectListByTwoColumn("WorkLocation", "company.id", company.getId().toString(), "name", workLocationBean.getName().toString());
			
			if(workLocationList.size() > 0) {
				model.put("message", "newWorkLocationForm.htm");
				return new ModelAndView("failed", model);
			} else {
				workLocationBean.setCompany(company);
				commonService.saveOrUpdateModelObjectToDB(workLocationBean);
			}
		}
		
		
		model.put("message", "newWorkLocationForm.htm");
		return new ModelAndView("success", model);
	}
	
	@RequestMapping(value = "/deleteWorkLocation.htm", method = RequestMethod.GET)
	public ModelAndView deleteWorkLocation (WorkLocation workLocationBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		commonService.deleteAnObjectById("WorkLocation", workLocationBean.getId());
		
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("message", "newWorkLocationForm.htm");
		
		return new ModelAndView("success", model);		
	}	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/checkWorkLocation.htm", method = RequestMethod.POST)
	 public @ResponseBody String checkWorkLocationExists(HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session)throws Exception {
		Company company = (Company) session.getAttribute("company");	
		String text = "true";		
		if(request.getParameter("name") != null){
			
			List<WorkLocation> holidayList = (List<WorkLocation>)(Object)commonService.getObjectListByTwoColumn("WorkLocation", "company.id", company.getId().toString(), "name", request.getParameter("name").toString());
			
			if(holidayList.size() > 0){ 
				text = "false";
			}
		}	 
		return text;
	 }
	
}