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

import com.nazdaq.payrms.model.BloodGroup;
import com.nazdaq.payrms.service.CommonService;
import com.nazdaq.payrms.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class BloodGroupController implements Constants{

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${cc.email.addresss}")
	String ccEmailAddresss;
	
	@Value("${common.email.address}")
	String commonEmailAddress;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getBloodGroup", method = RequestMethod.GET)
	public ModelAndView bgForm (@ModelAttribute("bgForm") BloodGroup bgBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		List<BloodGroup> bgList = (List<BloodGroup>)(Object)
				commonService.getAllObjectList("BloodGroup");		
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("bgList", bgList);		
		return new ModelAndView("addBG", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editBG.htm", method = RequestMethod.GET)
	public ModelAndView editBG (@ModelAttribute("bgForm") BloodGroup bgBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		List<BloodGroup> bgList = (List<BloodGroup>)(Object)
				commonService.getAllObjectList("BloodGroup");	
		BloodGroup bloodGroup = (BloodGroup)commonService.getAnObjectByAnyUniqueColumn("BloodGroup", "id", bgBean.getId().toString());
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("bgList", bgList);
		model.put("edit", true);
		model.put("bloodGroup", bloodGroup);
		return new ModelAndView("addBG", model);		
	}
	
	@RequestMapping(value = "/saveBG.htm", method = RequestMethod.POST)
	public ModelAndView saveOrUpdateDepartment (@ModelAttribute("bgForm") BloodGroup bgBean, Principal principal) {	
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Map <String, Object> model = new HashMap<String, Object>();	
		
		if(bgBean.getId() != null) {
			BloodGroup bloodGroup = (BloodGroup)commonService.getAnObjectByAnyUniqueColumn("BloodGroup", "id", bgBean.getId().toString());
			BloodGroup bgByName = (BloodGroup)commonService.getAnObjectByAnyUniqueColumn("BloodGroup", "name", bgBean.getName().toString());
			if(bgByName != null) {
				if(!bgByName.getId().toString().equals(bloodGroup.getId().toString())) {
					model.put("message", "getBloodGroup");
					return new ModelAndView("failed", model);
				}
				
			}else {
				bloodGroup.setName(bgBean.getName());
				commonService.saveOrUpdateModelObjectToDB(bloodGroup);
			}
		} else {
			BloodGroup bloodGroup = (BloodGroup)commonService.getAnObjectByAnyUniqueColumn("BloodGroup", "name", bgBean.getName().toString());
			if(bloodGroup != null) {
				model.put("message", "getBloodGroup");
				return new ModelAndView("failed", model);
			} else {
				commonService.saveOrUpdateModelObjectToDB(bgBean);
			}
		}
		
		
		model.put("message", "getBloodGroup");
		return new ModelAndView("success", model);
	}
	
	@RequestMapping(value = "/deleteBG.htm", method = RequestMethod.GET)
	public ModelAndView deleteDepartment (BloodGroup bgBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		commonService.deleteAnObjectById("BloodGroup", bgBean.getId());
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("message", "getBloodGroup");
		return new ModelAndView("success", model);		
	}	
	
	@RequestMapping(value = "/checkBG.htm", method = RequestMethod.POST)
	 public @ResponseBody String checkBloodGroupExists(HttpServletRequest request, HttpServletResponse response, Principal principal)throws Exception {
		String text = "true";		
		if(request.getParameter("name") != null){
			BloodGroup bloodGroup = (BloodGroup)commonService.getAnObjectByAnyUniqueColumn("BloodGroup", "name", request.getParameter("name").toString());
			if(bloodGroup != null){ 
				text = "false";
			}
		}	 
		return text;
	 }
	
}