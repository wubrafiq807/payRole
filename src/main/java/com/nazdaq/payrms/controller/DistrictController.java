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

import com.nazdaq.payrms.model.District;
import com.nazdaq.payrms.service.CommonService;
import com.nazdaq.payrms.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class DistrictController implements Constants{

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${cc.email.addresss}")
	String ccEmailAddresss;
	
	@Value("${common.email.address}")
	String commonEmailAddress;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newDistrictForm.htm", method = RequestMethod.GET)
	public ModelAndView districtForm (@ModelAttribute("districtForm") District districtBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		List<District> districtList = (List<District>)(Object)
				commonService.getAllObjectList("District");		
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("districtList", districtList);		
		return new ModelAndView("addDistrict", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editDistrict.htm", method = RequestMethod.GET)
	public ModelAndView editDistrict (@ModelAttribute("districtForm") District districtBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		List<District> districtList = (List<District>)(Object)
				commonService.getAllObjectList("District");	
		District district = (District)commonService.getAnObjectByAnyUniqueColumn("District", "id", districtBean.getId().toString());
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("districtList", districtList);
		model.put("edit", true);
		model.put("district", district);
		return new ModelAndView("addDistrict", model);		
	}
	
	@RequestMapping(value = "/saveDistrict.htm", method = RequestMethod.POST)
	public ModelAndView saveOrUpdateDepartment (@ModelAttribute("districtForm") District districtBean, Principal principal) {	
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Map <String, Object> model = new HashMap<String, Object>();	
		
		if(districtBean.getId() != null) {
			District district = (District)commonService.getAnObjectByAnyUniqueColumn("District", "id", districtBean.getId().toString());
			District districtByName = (District)commonService.getAnObjectByAnyUniqueColumn("District", "name", districtBean.getName().toString());
			if(districtByName != null) {
				if(!districtByName.getId().toString().equals(district.getId().toString())) {
					model.put("message", "newDistrictForm.htm");
					return new ModelAndView("failed", model);
				}
				
			}else {
				district.setName(districtBean.getName());
				commonService.saveOrUpdateModelObjectToDB(district);
			}
		} else {
			District district = (District)commonService.getAnObjectByAnyUniqueColumn("District", "name", districtBean.getName().toString());
			if(district != null) {
				model.put("message", "newDistrictForm.htm");
				return new ModelAndView("failed", model);
			} else {
				commonService.saveOrUpdateModelObjectToDB(districtBean);
			}
		}
		
		
		model.put("message", "newDistrictForm.htm");
		return new ModelAndView("success", model);
	}
	
	@RequestMapping(value = "/deleteDistrict.htm", method = RequestMethod.GET)
	public ModelAndView deleteDistrict (District districtBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		commonService.deleteAnObjectById("District", districtBean.getId());
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("message", "newDistrictForm.htm");
		return new ModelAndView("success", model);		
	}	
	
	@RequestMapping(value = "/checkDistrict.htm", method = RequestMethod.POST)
	 public @ResponseBody String checkDistrictExists(HttpServletRequest request, HttpServletResponse response, Principal principal)throws Exception {
		String text = "true";		
		if(request.getParameter("name") != null){
			District district = (District)commonService.getAnObjectByAnyUniqueColumn("District", "name", request.getParameter("name").toString());
			if(district != null){ 
				text = "false";
			}
		}	 
		return text;
	 }
	
}