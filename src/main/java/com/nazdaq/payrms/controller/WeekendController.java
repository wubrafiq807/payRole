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
import com.nazdaq.payrms.model.Weekend;
import com.nazdaq.payrms.service.CommonService;
import com.nazdaq.payrms.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class WeekendController implements Constants{

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${cc.email.addresss}")
	String ccEmailAddresss;
	
	@Value("${common.email.address}")
	String commonEmailAddress;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newWeekendForm.htm", method = RequestMethod.GET)
	public ModelAndView weekendForm (@ModelAttribute("weekendForm") Weekend weekendBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		Company company = (Company) session.getAttribute("company");
		//List<Weekend> weekendList = (List<Weekend>)(Object) commonService.getAllObjectList("Weekend");
		List<Weekend> weekendList = (List<Weekend>)(Object) commonService.getObjectListByAnyColumn("Weekend", "company.id", company.getId().toString());
		
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("weekendList", weekendList);		
		return new ModelAndView("addWeekend", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editWeekend.htm", method = RequestMethod.GET)
	public ModelAndView editWeekend (@ModelAttribute("weekendForm") Weekend weekendBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		Company company = (Company) session.getAttribute("company");
		//List<Weekend> weekendList = (List<Weekend>)(Object) commonService.getAllObjectList("Weekend");
		List<Weekend> weekendList = (List<Weekend>)(Object) commonService.getObjectListByAnyColumn("Weekend", "company.id", company.getId().toString());
		
		Weekend weekend = (Weekend)commonService.getAnObjectByAnyUniqueColumn("Weekend", "id", weekendBean.getId().toString());
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("weekendList", weekendList);
		model.put("edit", true);
		model.put("weekend", weekend);
		return new ModelAndView("addWeekend", model);		
	}
	
	@RequestMapping(value = "/saveWeekend.htm", method = RequestMethod.POST)
	public ModelAndView saveOrUpdateWeekend (@ModelAttribute("weekendForm") Weekend weekendBean, Principal principal, HttpSession session) {	
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		Company company = (Company) session.getAttribute("company");
		Map <String, Object> model = new HashMap<String, Object>();	
		
		if(weekendBean.getId() != null) {
			Weekend weekend = (Weekend)commonService.getAnObjectByAnyUniqueColumn("Weekend", "id", weekendBean.getId().toString());
			//Weekend weekendByName = (Weekend)commonService.getAnObjectByAnyUniqueColumn("Weekend", "name", weekendBean.getName().toString());
			List<Weekend> weekendList = (List<Weekend>)(Object) commonService.getObjectListByTwoColumn("Weekend", "company.id", company.getId().toString(), "name", weekendBean.getName().toString());
			if(weekendList.size() > 0) {
				Weekend weekendByName = weekendList.get(0);
				if(!weekendByName.getId().toString().equals(weekend.getId().toString())) {
					model.put("message", "newWeekendForm.htm");
					return new ModelAndView("failed", model);
				}
				
			}else {
				weekend.setName(weekendBean.getName());
				commonService.saveOrUpdateModelObjectToDB(weekend);
			}
		} else {
			//Weekend weekend = (Weekend)commonService.getAnObjectByAnyUniqueColumn("Weekend", "name", weekendBean.getName().toString());
			List<Weekend> weekendList = (List<Weekend>)(Object) commonService.getObjectListByTwoColumn("Weekend", "company.id", company.getId().toString(), "name", weekendBean.getName().toString());
			if(weekendList.size() >0) {
				model.put("message", "newWeekendForm.htm");
				return new ModelAndView("failed", model);
			} else {
				weekendBean.setCompany(company);
				commonService.saveOrUpdateModelObjectToDB(weekendBean);
			}
		}
		
		
		model.put("message", "newWeekendForm.htm");
		return new ModelAndView("success", model);
	}
	
	@RequestMapping(value = "/deleteWeekend.htm", method = RequestMethod.GET)
	public ModelAndView deleteWeekend (Weekend weekendBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		commonService.deleteAnObjectById("Weekend", weekendBean.getId());
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("message", "newWeekendForm.htm");
		return new ModelAndView("success", model);		
	}	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/checkWeekend.htm", method = RequestMethod.POST)
	 public @ResponseBody String checkWeekendExists(HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session)throws Exception {
		String text = "true";		
		Company company = (Company) session.getAttribute("company");
		if(request.getParameter("name") != null){
			//Weekend weekend = (Weekend)commonService.getAnObjectByAnyUniqueColumn("Weekend", "name", request.getParameter("name").toString());
			List<Weekend> weekendList = (List<Weekend>)(Object) commonService.getObjectListByTwoColumn("Weekend", "company.id", company.getId().toString(), "name", request.getParameter("name").toString());
			if(weekendList.size() > 0){ 
				text = "false";
			}
		}	 
		return text;
	 }
	
}