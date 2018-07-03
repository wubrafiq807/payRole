package com.nazdaq.payrms.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nazdaq.payrms.model.BloodGroup;
import com.nazdaq.payrms.model.Company;
import com.nazdaq.payrms.model.Department;
import com.nazdaq.payrms.model.Designation;
import com.nazdaq.payrms.model.District;
import com.nazdaq.payrms.model.Employee;
import com.nazdaq.payrms.model.Holiday;
import com.nazdaq.payrms.model.PaySite;
import com.nazdaq.payrms.model.Religion;
import com.nazdaq.payrms.model.WorkLocation;
import com.nazdaq.payrms.service.CommonService;
import com.nazdaq.payrms.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class EmployeeController implements Constants {

	@Autowired
	private CommonService commonService;

	@Autowired
	private JavaMailSender mailSender;

	@Value("${cc.email.addresss}")
	String ccEmailAddresss;

	@Value("${common.email.address}")
	String commonEmailAddress;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newEmployeeForm.htm", method = RequestMethod.GET)
	public ModelAndView employeeForm(@ModelAttribute("employeeForm") Employee employeeBean, Principal principal,
			HttpSession session) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}

		Map<String, Object> model = new HashMap<String, Object>();
		List<BloodGroup> bloodGroupList = (List<BloodGroup>) (Object) commonService.getAllObjectList("BloodGroup");
		List<District> districtList = (List<District>) (Object) commonService.getAllObjectList("District");
		List<Department> departmentList = (List<Department>) (Object) commonService.getAllObjectList("Department");
		List<Designation> designationList = (List<Designation>) (Object) commonService.getAllObjectList("Designation");
		List<Religion> religionList = (List<Religion>) (Object) commonService.getAllObjectList("Religion");
		List<PaySite> paySiteList = (List<PaySite>) (Object) commonService.getAllObjectList("PaySite");
		List<WorkLocation> workLocationList = (List<WorkLocation>) (Object) commonService
				.getAllObjectList("WorkLocation");
		model.put("bloodGroupList", bloodGroupList);
		model.put("districtListTotal", districtList);
		model.put("departmentList", departmentList);
		model.put("designationList", designationList);
		model.put("religionList", religionList);
		model.put("paySiteList", paySiteList);
		model.put("workLocationList", workLocationList);

		return new ModelAndView("addEmployee", model);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editEmployee.htm", method = RequestMethod.GET)
	public ModelAndView editHoliday(@ModelAttribute("employeeForm") Employee employeeBean, Principal principal,
			HttpSession session, HttpServletRequest request) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}

		Employee employee = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "id",
				request.getParameter("id").toString());
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("edit", true);
		model.put("employee", employee);
		List<BloodGroup> bloodGroupList = (List<BloodGroup>) (Object) commonService.getAllObjectList("BloodGroup");
		List<District> districtList = (List<District>) (Object) commonService.getAllObjectList("District");
		List<Department> departmentList = (List<Department>) (Object) commonService.getAllObjectList("Department");
		List<Designation> designationList = (List<Designation>) (Object) commonService.getAllObjectList("Designation");
		List<Religion> religionList = (List<Religion>) (Object) commonService.getAllObjectList("Religion");
		List<PaySite> paySiteList = (List<PaySite>) (Object) commonService.getAllObjectList("PaySite");
		List<WorkLocation> workLocationList = (List<WorkLocation>) (Object) commonService
				.getAllObjectList("WorkLocation");
		model.put("bloodGroupList", bloodGroupList);
		model.put("districtListTotal", districtList);
		model.put("departmentList", departmentList);
		model.put("designationList", designationList);
		model.put("religionList", religionList);
		model.put("paySiteList", paySiteList);
		model.put("workLocationList", workLocationList);
		return new ModelAndView("addEmployee", model);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveEmployee.htm", method = RequestMethod.POST)
	public ModelAndView saveOrUpdateEmployee(@ModelAttribute("employeeForm") Employee employeeBean,
			@RequestParam CommonsMultipartFile file, Principal principal, HttpSession session) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		Company company = (Company) session.getAttribute("company");

		if (employeeBean.getId() != null) {
			Employee employee = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "id",
					employeeBean.getId().toString());
			// employee.setBloodGroup(bloodGroup);
			Department department = (Department) commonService.getAnObjectByAnyUniqueColumn("Department", "id",
					employeeBean.getDepartmentId().toString());
			Designation designation = (Designation) commonService.getAnObjectByAnyUniqueColumn("Designation", "id",
					employeeBean.getDesignationId().toString());
			BloodGroup bloodGroup = (BloodGroup) commonService.getAnObjectByAnyUniqueColumn("BloodGroup", "id",
					employeeBean.getBloodGroupId().toString());
			District district = (District) commonService.getAnObjectByAnyUniqueColumn("District", "id",
					employeeBean.getDistrictId().toString());
			Religion religion = (Religion) commonService.getAnObjectByAnyUniqueColumn("Religion", "id",
					employeeBean.getReligionId().toString());
			WorkLocation workLocation = (WorkLocation) commonService.getAnObjectByAnyUniqueColumn("WorkLocation", "id",
					employeeBean.getWorkLocationId().toString());
			PaySite paySite = (PaySite) commonService.getAnObjectByAnyUniqueColumn("PaySite", "id",
					employeeBean.getPaySiteId().toString());
			employee.setFullname(employeeBean.getFullname());
			employee.setFatherName(employeeBean.getFatherName());
			employee.setMotherName(employeeBean.getMotherName());
			employee.setMeritalStatus(employeeBean.getMeritalStatus());
			employee.setDateOfBirth(employeeBean.getDateOfBirth());
			employee.setJoiningDate(employeeBean.getJoiningDate());
			employee.setBankAccNo(employeeBean.getBankAccNo());
			employee.setEmailAddress(employeeBean.getEmailAddress());
			employee.setGender(employeeBean.getGender());
			employee.setPermanentAddress(employeeBean.getPermanentAddress());
			employee.setPresentAddress(employeeBean.getPresentAddress());
			employee.setPersonalPhone(employeeBean.getPersonalPhone());
			employee.setWorkPhone(employeeBean.getWorkPhone());
			
			employee.setCompany(company);
			employee.setDepartment(department);
			employee.setDesignation(designation);
			employee.setBloodGroup(bloodGroup);
			employee.setDistrict(district);
			employee.setReligion(religion);
			employee.setPaySite(paySite);
			employee.setWorkLocation(workLocation);

			employee.setStatus(employeeBean.getStatus());
			employee.setMeritalStatus(employeeBean.getMeritalStatus());
			employee.setModifiedDate(new Date());
			employee.setModifiedBy(principal.getName());
						
			try {
				if (!file.isEmpty()) {
					this.processarAvatar(employee, file);
					employee.setLogo(employee.getEmployeeId());
				}

			} catch (Exception e) {
				System.out.println(e);
			}

			commonService.saveOrUpdateModelObjectToDB(employee);
		} else {
			List<Employee> employeeList = (List<Employee>) (Object) commonService.getObjectListByTwoColumn("Employee",
					"company.id", company.getId().toString(), "employeeId", employeeBean.getEmployeeId().toString());
			if (employeeList.size() > 0) {
				
				model.put("message", "newEmployeeForm.htm");
				return new ModelAndView("failed", model);
			} else {
				Department department = (Department) commonService.getAnObjectByAnyUniqueColumn("Department", "id",
						employeeBean.getDepartmentId().toString());
				Designation designation = (Designation) commonService.getAnObjectByAnyUniqueColumn("Designation", "id",
						employeeBean.getDesignationId().toString());
				BloodGroup bloodGroup = (BloodGroup) commonService.getAnObjectByAnyUniqueColumn("BloodGroup", "id",
						employeeBean.getBloodGroupId().toString());
				District district = (District) commonService.getAnObjectByAnyUniqueColumn("District", "id",
						employeeBean.getDistrictId().toString());
				Religion religion = (Religion) commonService.getAnObjectByAnyUniqueColumn("Religion", "id",
						employeeBean.getReligionId().toString());
				WorkLocation workLocation = (WorkLocation) commonService.getAnObjectByAnyUniqueColumn("WorkLocation",
						"id", employeeBean.getWorkLocationId().toString());
				PaySite paySite = (PaySite) commonService.getAnObjectByAnyUniqueColumn("PaySite", "id",
						employeeBean.getPaySiteId().toString());

				employeeBean.setCompany(company);
				employeeBean.setDepartment(department);
				employeeBean.setDesignation(designation);
				employeeBean.setBloodGroup(bloodGroup);
				employeeBean.setDistrict(district);
				employeeBean.setReligion(religion);
				employeeBean.setPaySite(paySite);
				employeeBean.setWorkLocation(workLocation);
				
				try {
					if (!file.isEmpty()) {
						this.processarAvatar(employeeBean, file);
						employeeBean.setLogo(employeeBean.getEmployeeId());
					}

				} catch (Exception e) {
					System.out.println(e);
				}
				

				employeeBean.setCreatedBy(principal.getName());
				employeeBean.setCreatedDate(new Date());
				commonService.saveOrUpdateModelObjectToDB(employeeBean);
			}
		}

		//model.put("message", "newEmployeeForm.htm");
		return new ModelAndView("redirect:/employeeList.htm");
	//	return new ModelAndView("success", model);
	}

	private void processarAvatar(Employee employee, MultipartFile photo) {
		File diretorio = new File("/payrole/upload/photo");
		if (!diretorio.exists()) {
			diretorio.mkdirs();
		}
		try {
			FileOutputStream arquivo = new FileOutputStream(
					diretorio.getAbsolutePath() + "/" + employee.getEmployeeId() + ".jpg");
			arquivo.write(photo.getBytes());
			arquivo.close();
		} catch (IOException ex) {

		}

	}
	
	@RequestMapping("/photo/{employeeId}")
	@ResponseBody
	public byte[] photo(@PathVariable("employeeId") String employeeId) throws IOException {
		File arquivo = new File("/payrole/upload/photo/" + employeeId + ".jpg");

		if (!arquivo.exists()) {
			arquivo = new File("/payrole/upload/photo/photo.jpg");
		}

		byte[] resultado = new byte[(int) arquivo.length()];
		FileInputStream input = new FileInputStream(arquivo);
		input.read(resultado);
		input.close();
		return resultado;
	}

	@RequestMapping(value = "/checkUnicKEmailForEmployee", method = RequestMethod.POST)
	private @ResponseBody void checkUnicKEmail(HttpServletRequest request, Principal principal,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, Exception {
		String toJson = "";
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		JsonObject myObj = new JsonObject();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		Employee company = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "emailAddress",
				request.getParameter("emailAddress").toString());
		JsonElement reagentobj = gson.toJsonTree(company);
		myObj.add("companyInfo", reagentobj);
		out.println(myObj.toString());

		out.close();

	}

	@RequestMapping(value = "/checkUnickEmployeeId", method = RequestMethod.POST)
	private @ResponseBody void checkUnickEmployeeId(HttpServletRequest request, Principal principal,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, Exception {
		String toJson = "";
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		JsonObject myObj = new JsonObject();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		Employee company = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "employeeId",
				request.getParameter("employeeId").toString());
		JsonElement reagentobj = gson.toJsonTree(company);
		myObj.add("companyInfo", reagentobj);
		out.println(myObj.toString());

		out.close();

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/employeeList.htm", method = RequestMethod.GET)
	public ModelAndView employeeList(Employee employeeBean, Principal principal, HttpSession session) {
	
		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}
		Company company = (Company) session.getAttribute("company");
		List<Employee> employeeList = (List<Employee>) (Object) commonService.getObjectListByAnyColumn("Employee", "company.id", company.getId().toString());
		Map<String, Object> model = new HashMap<String, Object>();

		model.put("employeeList", employeeList);
		return new ModelAndView("employeeList", model);
	}

	@RequestMapping(value = "/uploadEmployeeForm.htm", method = RequestMethod.GET)
	public ModelAndView uploadEmployeeForm(@ModelAttribute("employeeForm") Employee employeeBean, Principal principal,
			HttpSession session) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}

		Map<String, Object> model = new HashMap<String, Object>();
		return new ModelAndView("uploadEmployeeForm", model);
	}

	@RequestMapping(value = "/uploadEmployee.htm", method = RequestMethod.GET)
	public ModelAndView uploadEmployee(@ModelAttribute("employeeForm") Employee employeeBean, Principal principal,
			HttpSession session) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}
		Map<String, Object> model = new HashMap<String, Object>();
		return new ModelAndView("redirect:/employeeList.htm", model);
	}

	@RequestMapping(value = "/uploadIncrementForm.htm", method = RequestMethod.GET)
	public ModelAndView uploadIncrementForm(@ModelAttribute("employeeForm") Employee employeeBean, Principal principal,
			HttpSession session) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}

		Map<String, Object> model = new HashMap<String, Object>();
		return new ModelAndView("uploadIncrementForm", model);
	}

	@RequestMapping(value = "/uploadIncrement.htm", method = RequestMethod.GET)
	public ModelAndView uploadIncrement(@ModelAttribute("employeeForm") Employee employeeBean, Principal principal,
			HttpSession session) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}
		Map<String, Object> model = new HashMap<String, Object>();
		return new ModelAndView("redirect:/employeeList.htm", model);
	}

}
