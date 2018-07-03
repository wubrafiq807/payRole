package com.nazdaq.payrms.controller;

import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nazdaq.payrms.beans.UserBean;
import com.nazdaq.payrms.beans.UserRoleBean;
import com.nazdaq.payrms.model.Employee;
import com.nazdaq.payrms.model.User;
import com.nazdaq.payrms.model.UserRole;
import com.nazdaq.payrms.service.CommonService;
import com.nazdaq.payrms.service.UserRoleService;
import com.nazdaq.payrms.service.UserService;
import com.nazdaq.payrms.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class UserController implements Constants {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRoleService userRoleService;
	
	@Autowired
	private CommonService commonService;

	@Autowired
	private JavaMailSender mailSender;

	@Value("${cc.email.addresss}")
	String ccEmailAddresss;

	@Value("${common.email.address}")
	String commonEmailAddress;

	java.util.Date date = new java.util.Date();
	Timestamp curTime = new Timestamp(date.getTime());

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addUser.htm", method = RequestMethod.GET)
	public ModelAndView addUser(@ModelAttribute("command") UserBean userBean, BindingResult result) {
		Map<String, Object> model = new HashMap<String, Object>();
			
		List<Employee> empList = (List<Employee>) (Object) commonService.getObjectListByAnyColumn("Employee", "status", "1");
		model.put("users",  prepareListofBean(userService.listUser()));
		model.put("userRoles", userRoleService.getUserRoleName());
		model.put("employees", empList);
		return new ModelAndView("addUser", model);
	}

	private User prepareModel(UserBean userBean) {
		User user = new User();
		user.setuName(userBean.getuName());
		user.setId(userBean.getId());
		user.setPassword(userBean.getPass());
		user.setEmail(userBean.getEmail());
		user.setStatus(userBean.getStatus());
		user.setFirstName(userBean.getfName());
		user.setLastName(userBean.getlName());
		user.setDescription(userBean.getDesc());
		user.setInsertDate(curTime);
		user.setUpdateDate(curTime);
		userBean.setId(null);
		return user;
	}

	private List<UserBean> prepareListofBean(List<User> users) {
		List<UserBean> beans = null;
		if (users != null && !users.isEmpty()) {
			beans = new ArrayList<UserBean>();
			UserBean bean = null;
			for (User user : users) {
				bean = new UserBean();
				bean.setuName(user.getuName());
				bean.setId(user.getId());
				bean.setPass(user.getPassword());
				bean.setEmail(user.getEmail());
				bean.setStatus(user.getStatus());
				bean.setfName(user.getFirstName());
				bean.setlName(user.getLastName());
				bean.setDesc(user.getDescription());
				bean.setInsertDate(curTime);
				bean.setUpdateDate(curTime);
				beans.add(bean);
			}
		}
		return beans;
	}

	private UserBean prepareUserBean(User user) {
		UserBean bean = new UserBean();
		bean.setuName(user.getuName());
		bean.setId(user.getId());
		bean.setPass(user.getPassword());
		bean.setEmail(user.getEmail());
		bean.setStatus(user.getStatus());
		bean.setfName(user.getFirstName());
		bean.setlName(user.getLastName());
		bean.setDesc(user.getDescription());
		bean.setInsertDate(curTime);
		bean.setUpdateDate(curTime);
		return bean;
	}

	private String sendMail(String a, String b, String c) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(a);
		email.setSubject(b);
		email.setText(c);
		// sends the e-mail
		mailSender.send(email);
		// forwards to the view named "Result"
		return "Result";
	}

	@RequestMapping(value = "/checkUser.htm", method = RequestMethod.POST)
	public @ResponseBody String checkUserExists(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String text = "true";
		/* String officeId=request.getParameter("officeId").trim(); */
		if (request.getParameter("username") != null) {
			String username = request.getParameter("username");
			if (userService.isExistsUser(username)) {
				text = "false";
			}
		}
		return text;

	}
	
	@RequestMapping(value = "/saveUser.html", method = RequestMethod.POST)
	public ModelAndView saveUser(@ModelAttribute("command") UserBean userBean,  
			BindingResult result, HttpServletRequest request) throws NoSuchAlgorithmException {
		
		String s= "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/login";
		User user = prepareModel(userBean);
		
		//PasswordEncoderMD5 pemd = new PasswordEncoderMD5();
		//String password = pemd.getEncryptedPassword(userBean.getPass());
		//user.setPassword(password);
		
		String authority=request.getParameter("auth");
		
		boolean checkId=true;
		
		  if(null==user.getId()){
			  checkId=false;
		  }
		  Employee employee = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "id", userBean.getEmpId().toString());
		  user.setEmployee(employee);
		  userService.addUser(user);
		  insertUserRole(user,authority);
		 
		  if(!checkId){
			
			if(null!= user.getEmail()){
			//String s=sendMail(user.getEmail().toString(),"test","your are registerd");			
			//sendUserDetailsMail(user, s);
		}
			
	}
		
	String message="addUser";		
	return new ModelAndView("success", "message", message);
		
	}
	
	@RequestMapping(value = "/updateUser.htm", method = RequestMethod.POST)
	public ModelAndView updateUser(@ModelAttribute("command") UserBean userBean,  
			BindingResult result, HttpServletRequest request, Principal p) throws NoSuchAlgorithmException {
		if(userBean.getId() != null) {
			//update user
			User user = userService.getUser(userBean.getId());
			//PasswordEncoderMD5 pemd = new PasswordEncoderMD5();
			//String password = pemd.getEncryptedPassword(userBean.getPass());
			//user.setPassword(password);
			user.setPassword(userBean.getPass());
			user.setEmail(userBean.getEmail());
			user.setFirstName(userBean.getfName());
			user.setLastName(userBean.getlName());
			user.setDescription(userBean.getDesc());
			user.setStatus(userBean.getStatus());
			user.setUpdateDate(curTime);
			userService.addUser(user);
			
			//update user-role
			UserRole userRole = (UserRole) commonService.getAnObjectByAnyUniqueColumn("UserRole", "userId", user.getId().toString());			
			userRole.setAuthority(userBean.getAuth());
			userRoleService.addUserRole(userRole);
			
			String message="addUser";		
			return new ModelAndView("success", "message", message);
		}else {
			return new ModelAndView("redirect:/addUser");
		}
		
		
	}
	

	private void insertUserRole(User u, String authority){
		UserRole ur=new UserRole();
		ur.setUserId(u.getId());
		ur.setAuthority(authority);
		userRoleService.addUserRole(ur);
	}

	@RequestMapping(value="/users.htm", method = RequestMethod.GET)
	public ModelAndView listUsers() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("users",  prepareListofBean(userService.listUser()));
		return new ModelAndView("usersList", model);
	}
	
	@RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
	public ModelAndView editUser(@ModelAttribute("command")  UserBean userBean,
			BindingResult result) {
		userService.deleteUser(prepareModel(userBean));
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("user", null);
		model.put("users",  prepareListofBean(userService.listUser()));
		return new ModelAndView("addUser", model);
	}
	
	@RequestMapping(value = "/editUser", method = RequestMethod.GET)
	public ModelAndView deleteUser(@ModelAttribute("command") UserBean userBean,
			BindingResult result) {
		Map<String, Object> model = new HashMap<String, Object>();
		UserBean user = prepareUserBean(userService.getUser(userBean.getId()));
		UserRole ur = (UserRole) commonService.getAnObjectByAnyUniqueColumn("UserRole", "userId", user.getId().toString());
		user.setAuth(ur.getAuthority());
		model.put("user", user);
		model.put("users",  prepareListofBean(userService.listUser()));
		
		model.put("userRoles", userRoleService.getUserRoleName());
		return new ModelAndView("editUser", model);
	}
	
	private void sendUserDetailsMail(User users, String s){
		// Create a new StringBuilder.
		StringBuilder builder = new StringBuilder();
		 builder.append("Your Details\n\n");
		 builder.append("User Id:  ").append(users.getuName()).append("   Password: ").append(users.getPassword()).append("\n\n");
		 builder.append(s);
		 // Convert to string.
		 String result = builder.toString();
		
		 sendMail(users.getEmail().toString(),"YOU ARE REGISTERED!!!", result);
	}
	
	
	@RequestMapping(value = "/addURole.htm", method = RequestMethod.GET)
	public ModelAndView addUserRole(@ModelAttribute("command")  UserRoleBean userRoleBean,
			BindingResult result) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("users",  prepareListofBean(userService.listUser()));
		
		List<UserRoleBean> uRoleBeans=prepareListofBeanU(userRoleService.listUserRole());
		
		
		for(UserRoleBean ub:uRoleBeans){
			User u=userService.getUser(ub.getUserId());
			ub.setValue4(u.getuName());
		}
		model.put("userRoles", uRoleBeans);
		
		return new ModelAndView("addUserRole", model);
	}

	private List<UserRoleBean> prepareListofBeanU(List<UserRole> userRoles) {
		List<UserRoleBean> beans = null;
		if (userRoles != null && !userRoles.isEmpty()) {
			beans = new ArrayList<UserRoleBean>();
			UserRoleBean bean = null;
			for (UserRole userRole : userRoles) {
				bean = new UserRoleBean();
				bean.setUserId(userRole.getUserId());
				bean.setAuthority(userRole.getAuthority());
				bean.setUserRoleId(userRole.getUserRoleId());
				beans.add(bean);
			}
		}
		return beans;
	}

}
