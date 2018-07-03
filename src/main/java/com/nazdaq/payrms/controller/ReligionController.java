package com.nazdaq.payrms.controller;

import java.security.Principal;
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

import com.nazdaq.payrms.model.Religion;
import com.nazdaq.payrms.service.CommonService;
import com.nazdaq.payrms.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class ReligionController implements Constants{

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${cc.email.addresss}")
	String ccEmailAddresss;
	
	@Value("${common.email.address}")
	String commonEmailAddress;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newReligionForm.htm", method = RequestMethod.GET)
	public ModelAndView religionForm (@ModelAttribute("religionForm") Religion religionBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		List<Religion> religionList = (List<Religion>)(Object)
				commonService.getAllObjectList("Religion");		
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("religionList", religionList);		
		return new ModelAndView("addReligion", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editReligion.htm", method = RequestMethod.GET)
	public ModelAndView editReligion (@ModelAttribute("religionForm") Religion religionBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		List<Religion> religionList = (List<Religion>)(Object)
				commonService.getAllObjectList("Religion");	
		Religion religion = (Religion)commonService.getAnObjectByAnyUniqueColumn("Religion", "id", religionBean.getId().toString());
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("religionList", religionList);
		model.put("edit", true);
		model.put("religion", religion);
		return new ModelAndView("addReligion", model);		
	}
	
	@RequestMapping(value = "/saveReligion.htm", method = RequestMethod.POST)
	public ModelAndView saveOrUpdateReligion (@ModelAttribute("religionForm") Religion religionBean, Principal principal) {	
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Map <String, Object> model = new HashMap<String, Object>();	
		
		if(religionBean.getId() != null) {
			Religion religion = (Religion)commonService.getAnObjectByAnyUniqueColumn("Religion", "id", religionBean.getId().toString());
			Religion religionByName = (Religion)commonService.getAnObjectByAnyUniqueColumn("Religion", "religionName", religionBean.getReligionName().toString());
			if(religionByName != null) {
				if(!religionByName.getId().toString().equals(religion.getId().toString())) {
					model.put("message", "newReligionForm.htm");
					return new ModelAndView("failed", model);
				}
				
			}else {
				religion.setReligionName(religionBean.getReligionName());
				commonService.saveOrUpdateModelObjectToDB(religion);
			}
		} else {
			Religion religion = (Religion)commonService.getAnObjectByAnyUniqueColumn("Religion", "religionName", religionBean.getReligionName().toString());
			if(religion != null) {
				model.put("message", "newReligionForm.htm");
				return new ModelAndView("failed", model);
			} else {
				commonService.saveOrUpdateModelObjectToDB(religionBean);
			}
		}
		
		
		model.put("message", "newReligionForm.htm");
		return new ModelAndView("success", model);
	}
	
	@RequestMapping(value = "/deleteReligion.htm", method = RequestMethod.GET)
	public ModelAndView deleteReligion (Religion religionBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		commonService.deleteAnObjectById("Religion", religionBean.getId());
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("message", "newReligionForm.htm");
		return new ModelAndView("success", model);		
	}	
	
	@RequestMapping(value = "/checkReligion.htm", method = RequestMethod.POST)
	 public @ResponseBody String checkReligionExists(HttpServletRequest request, HttpServletResponse response, Principal principal)throws Exception {
		String text = "true";		
		if(request.getParameter("religionName") != null){
			Religion religion = (Religion)commonService.getAnObjectByAnyUniqueColumn("Religion", "religionName", request.getParameter("religionName").toString());
			if(religion != null){ 
				text = "false";
			}
		}	 
		return text;
	 }
	
}