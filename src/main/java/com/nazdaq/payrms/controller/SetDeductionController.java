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

import com.nazdaq.payrms.model.DeductionSetup;
import com.nazdaq.payrms.model.Company;
import com.nazdaq.payrms.service.CommonService;
import com.nazdaq.payrms.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class SetDeductionController implements Constants{

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${cc.email.addresss}")
	String ccEmailAddresss;
	
	@Value("${common.email.address}")
	String commonEmailAddress;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newDeductionSetupForm.htm", method = RequestMethod.GET)
	public ModelAndView deductionSetupForm (@ModelAttribute("deductionSetupForm") DeductionSetup deductionSetupBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Company company = (Company) session.getAttribute("company");
		
		List<DeductionSetup> deductionSetupList = (List<DeductionSetup>)(Object)commonService.getObjectListByAnyColumn("DeductionSetup", "company.id", company.getId().toString());
		
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("deductionSetupList", deductionSetupList);		
		return new ModelAndView("addDeductionSetup", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editDeductionSetup.htm", method = RequestMethod.GET)
	public ModelAndView editDeductionSetup (@ModelAttribute("deductionSetupForm") DeductionSetup deductionSetupBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Company company = (Company) session.getAttribute("company");
		
		List<DeductionSetup> deductionSetupList = (List<DeductionSetup>)(Object)commonService.getObjectListByAnyColumn("DeductionSetup", "company.id", company.getId().toString());
		
		DeductionSetup deductionSetup = (DeductionSetup)commonService.getAnObjectByAnyUniqueColumn("DeductionSetup", "id", deductionSetupBean.getId().toString());
		
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("deductionSetupList", deductionSetupList);
		model.put("edit", true);
		model.put("deductionSetup", deductionSetup);
		return new ModelAndView("addDeductionSetup", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveDeductionSetup.htm", method = RequestMethod.POST)
	public ModelAndView saveOrUpdateDeductionSetup (@ModelAttribute("deductionSetupForm") DeductionSetup deductionSetupBean, Principal principal, HttpSession session) {	
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Map <String, Object> model = new HashMap<String, Object>();	
		Company company = (Company) session.getAttribute("company");
		
		if(deductionSetupBean.getId() != null) {
			
			DeductionSetup deductionSetup = (DeductionSetup)commonService.getAnObjectByAnyUniqueColumn("DeductionSetup", "id", deductionSetupBean.getId().toString());
			deductionSetupBean.setCompany(deductionSetup.getCompany());
			List<DeductionSetup> deductionSetupList = (List<DeductionSetup>)(Object) 
					commonService.getObjectListByTwoColumn("DeductionSetup", "company.id", company.getId().toString(), "name", deductionSetupBean.getName().toString());
			
			if(deductionSetupList.size() > 0) {
				DeductionSetup allowanceSetupByName = deductionSetupList.get(0);
				if(!allowanceSetupByName.getId().toString().equals(deductionSetup.getId().toString())) {
					model.put("message", "newDeductionSetupForm.htm");
					return new ModelAndView("failed", model);
				}else {				
					commonService.saveOrUpdateModelObjectToDB(deductionSetupBean);
				}
				
			}else {					
				commonService.saveOrUpdateModelObjectToDB(deductionSetupBean);
			}
		} else {
			
			List<DeductionSetup> allowanceSetupList = (List<DeductionSetup>)(Object) 
					commonService.getObjectListByTwoColumn("DeductionSetup", "company.id", company.getId().toString(), "name", deductionSetupBean.getName().toString());
			
			if(allowanceSetupList.size() > 0) {
				model.put("message", "newDeductionSetupForm.htm");
				return new ModelAndView("failed", model);
			} else {
				
				deductionSetupBean.setCompany(company);				
				commonService.saveOrUpdateModelObjectToDB(deductionSetupBean);
			}
		}
		
		
		model.put("message", "newDeductionSetupForm.htm");
		return new ModelAndView("success", model);
	}
	
	@RequestMapping(value = "/deleteDeductionSetup.htm", method = RequestMethod.GET)
	public ModelAndView deleteDeductionSetup (DeductionSetup deductionSetupBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		commonService.deleteAnObjectById("DeductionSetup", deductionSetupBean.getId());
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("message", "newDeductionSetupForm.htm");
		return new ModelAndView("success", model);		
	}	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/checkDeductionSetup.htm", method = RequestMethod.POST)
	 public @ResponseBody String checkDeductionSetupExists(HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session)throws Exception {
		Company company = (Company) session.getAttribute("company");	
		String text = "true";		
		if(request.getParameter("name") != null){
			List<DeductionSetup> deductionSetupList = (List<DeductionSetup>)(Object) 
					commonService.getObjectListByTwoColumn("DeductionSetup", "company.id", company.getId().toString(), "name", request.getParameter("name").toString());
			if(deductionSetupList.size() > 0){ 
				text = "false";
			}
		}	 
		return text;
	 }
}
