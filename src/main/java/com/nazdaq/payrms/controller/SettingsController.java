package com.nazdaq.payrms.controller;

import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
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
import com.nazdaq.payrms.model.Settings;
import com.nazdaq.payrms.service.CommonService;
import com.nazdaq.payrms.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class SettingsController implements Constants{

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${cc.email.addresss}")
	String ccEmailAddresss;
	
	@Value("${common.email.address}")
	String commonEmailAddress;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newSettingsForm.htm", method = RequestMethod.GET)
	public ModelAndView settingsForm (@ModelAttribute("settingsForm") Settings settingsBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		Company company = (Company) session.getAttribute("company");		
		
		//List<Settings> settingsList = (List<Settings>)(Object) commonService.getAllObjectList("Settings");
		List<Settings> settingsList = (List<Settings>)(Object) commonService.getObjectListByAnyColumn("Settings", "company.id", company.getId().toString());
				
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("settingsList", settingsList);		
		return new ModelAndView("settingsForm", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editSettings.htm", method = RequestMethod.GET)
	public ModelAndView editSettings (@ModelAttribute("settingsForm") Settings settingsBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		Company company = (Company) session.getAttribute("company");		
		
		//List<Settings> settingsList = (List<Settings>)(Object) commonService.getAllObjectList("Settings");	
		List<Settings> settingsList = (List<Settings>)(Object) commonService.getObjectListByAnyColumn("Settings", "company.id", company.getId().toString());
		
		Settings settings = (Settings) commonService.getAnObjectByAnyUniqueColumn("Settings", "id", settingsBean.getId().toString());
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("settingsList", settingsList);
		model.put("edit", true);
		model.put("settings", settings);
		return new ModelAndView("settingsForm", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveSettings.htm", method = RequestMethod.POST)
	public ModelAndView saveOrUpdateSettings (@ModelAttribute("settingsForm") Settings settingsBean, Principal principal, HttpSession session) {	
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Company company = (Company) session.getAttribute("company");		
		Map <String, Object> model = new HashMap<String, Object>();	
		
		if(settingsBean.getBonusMonth1().trim().equals(settingsBean.getBonusMonth2().trim())) {
			model.put("message", "newSettingsForm.htm");
			return new ModelAndView("failed", model);
		}		
		
		
		if(settingsBean.getId() != null) {
			Settings settings = (Settings)commonService.getAnObjectByAnyUniqueColumn("Settings", "id", settingsBean.getId().toString());	
			settings.setBonusMonth1(settingsBean.getBonusMonth1());
			settings.setBonusMonth2(settingsBean.getBonusMonth2());
			settings.setWorkingDay(settingsBean.getWorkingDay());
			commonService.saveOrUpdateModelObjectToDB(settings);
		} else {
			Date now = new Date();
			Calendar calc = Calendar.getInstance();
			calc.setTime(now);
			Integer currentYear = calc.get(Calendar.YEAR);
			//Settings settingsByCompanyAndYear = (Settings)commonService.getAnObjectByAnyUniqueColumn("Settings", "company.id", company.getId().toString());
			List<Settings> settingsList = (List<Settings>)(Object) commonService.getObjectListByTwoColumn("Settings", "company.id", company.getId().toString(), "bonusYear", currentYear.toString());
			if(settingsList.size() > 0) {
				model.put("message", "newSettingsForm.htm");
				return new ModelAndView("failed", model);
			} else {
				settingsBean.setCompany(company);
				settingsBean.setBonusYear(currentYear.toString());
				commonService.saveOrUpdateModelObjectToDB(settingsBean);
			}
		}		
		
		model.put("message", "newSettingsForm.htm");
		return new ModelAndView("success", model);
	}
	
	@RequestMapping(value = "/deleteSettings.htm", method = RequestMethod.GET)
	public ModelAndView deleteSettings (Settings settingsBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		commonService.deleteAnObjectById("Settings", settingsBean.getId());
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("message", "newSettingsForm.htm");
		return new ModelAndView("success", model);		
	}
	
}
