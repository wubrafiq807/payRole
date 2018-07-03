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

import com.nazdaq.payrms.model.Branch;
import com.nazdaq.payrms.service.CommonService;
import com.nazdaq.payrms.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class BranchController implements Constants{

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${cc.email.addresss}")
	String ccEmailAddresss;
	
	@Value("${common.email.address}")
	String commonEmailAddress;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newBranchForm.htm", method = RequestMethod.GET)
	public ModelAndView branchForm (@ModelAttribute("branchForm") Branch branchBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		List<Branch> branchList = (List<Branch>)(Object)
				commonService.getAllObjectList("Branch");		
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("branchList", branchList);		
		return new ModelAndView("addBranch", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editBranch.htm", method = RequestMethod.GET)
	public ModelAndView editBranch (@ModelAttribute("branchForm") Branch branchBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		List<Branch> branchList = (List<Branch>)(Object)
				commonService.getAllObjectList("Branch");	
		Branch branch = (Branch)commonService.getAnObjectByAnyUniqueColumn("Branch", "id", branchBean.getId().toString());
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("branchList", branchList);
		model.put("edit", true);
		model.put("branch", branch);
		return new ModelAndView("addBranch", model);		
	}
	
	@RequestMapping(value = "/saveBranch.htm", method = RequestMethod.POST)
	public ModelAndView saveOrUpdateBranch (@ModelAttribute("branchForm") Branch branchBean, Principal principal) {	
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Map <String, Object> model = new HashMap<String, Object>();	
		
		if(branchBean.getId() != null) {
			Branch branch = (Branch)commonService.getAnObjectByAnyUniqueColumn("Branch", "id", branchBean.getId().toString());
			Branch branchByName = (Branch)commonService.getAnObjectByAnyUniqueColumn("Branch", "branchName", branchBean.getBranchName().toString());
			if(branchByName != null) {
				if(!branchByName.getId().toString().equals(branch.getId().toString())) {
					model.put("message", "newBranchForm.htm");
					return new ModelAndView("failed", model);
				}
				
			}else {
				branch.setBranchName(branchBean.getBranchName());
				commonService.saveOrUpdateModelObjectToDB(branch);
			}
		} else {
			Branch branch = (Branch)commonService.getAnObjectByAnyUniqueColumn("DesBranchignation", "branchName", branchBean.getBranchName().toString());
			if(branch != null) {
				model.put("message", "newBranchForm.htm");
				return new ModelAndView("failed", model);
			} else {
				commonService.saveOrUpdateModelObjectToDB(branchBean);
			}
		}
		
		
		model.put("message", "newBranchForm.htm");
		return new ModelAndView("success", model);
	}
	
	@RequestMapping(value = "/deleteBranch.htm", method = RequestMethod.GET)
	public ModelAndView deleteBranch (Branch branchBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		commonService.deleteAnObjectById("Branch", branchBean.getId());
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("message", "newBranchForm.htm");
		return new ModelAndView("success", model);		
	}	
	
	@RequestMapping(value = "/checkBranch.htm", method = RequestMethod.POST)
	 public @ResponseBody String checkBranchExists(HttpServletRequest request, HttpServletResponse response, Principal principal)throws Exception {
		String text = "true";		
		if(request.getParameter("branchName") != null){
			Branch branch = (Branch)commonService.getAnObjectByAnyUniqueColumn("Branch", "branchName", request.getParameter("branchName").toString());
			if(branch != null){ 
				text = "false";
			}
		}	 
		return text;
	 }
	
}