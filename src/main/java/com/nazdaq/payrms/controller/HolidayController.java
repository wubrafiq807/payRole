package com.nazdaq.payrms.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import com.nazdaq.payrms.model.Holiday;
import com.nazdaq.payrms.service.CommonService;
import com.nazdaq.payrms.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class HolidayController implements Constants {

	@Autowired
	private CommonService commonService;

	@Autowired
	private JavaMailSender mailSender;

	@Value("${cc.email.addresss}")
	String ccEmailAddresss;

	@Value("${common.email.address}")
	String commonEmailAddress;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newHolidayForm.htm", method = RequestMethod.GET)
	public ModelAndView holidayForm(@ModelAttribute("holidayForm") Holiday holidayBean, Principal principal,
			HttpSession session) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}

		Company company = (Company) session.getAttribute("company");

		// List<Holiday> holidayList =
		// (List<Holiday>)(Object)commonService.getAllObjectList("Holiday");
		List<Holiday> holidayList = (List<Holiday>) (Object) commonService.getObjectListByAnyColumn("Holiday",
				"company.id", company.getId().toString());

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("holidayList", holidayList);
		return new ModelAndView("addHoliday", model);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editHoliday.htm", method = RequestMethod.GET)
	public ModelAndView editHoliday(@ModelAttribute("holidayForm") Holiday holidayBean, Principal principal,
			HttpSession session) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}

		Company company = (Company) session.getAttribute("company");

		// List<Holiday> holidayList =
		// (List<Holiday>)(Object)commonService.getAllObjectList("Holiday");
		List<Holiday> holidayList = (List<Holiday>) (Object) commonService.getObjectListByAnyColumn("Holiday",
				"company.id", company.getId().toString());

		Holiday holiday = (Holiday) commonService.getAnObjectByAnyUniqueColumn("Holiday", "id",
				holidayBean.getId().toString());
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("holidayList", holidayList);
		model.put("edit", true);
		model.put("holiday", holiday);
		return new ModelAndView("addHoliday", model);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveHoliday.htm", method = RequestMethod.POST)
	public ModelAndView saveOrUpdateHoliday(@ModelAttribute("holidayForm") Holiday holidayBean, Principal principal,
			HttpSession session) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}

		Map<String, Object> model = new HashMap<String, Object>();
		Company company = (Company) session.getAttribute("company");
		// holidayBean.setCompany(company);
		List<LocalDate> totalDates = this.getDateList(holidayBean.getStartDate(), holidayBean.getEndDate());
		for (LocalDate localDate : totalDates) {
			DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
			String formattedString = localDate.format(formatter);
			Holiday holiday = (Holiday) commonService.getAnObjectByAnyUniqueColumn("Holiday", "holidayDate",
					formattedString);
			if (holiday == null) {
				Holiday holiday2 = new Holiday();
				holiday2.setHolidayDate(formattedString);
				holiday2.setCompany(company);
				holiday2.setName(holidayBean.getName());
				holiday2.setHolidayType(holidayBean.getHolidayType().toString());

				commonService.saveWithReturnId(holiday2);
			}
		}

		model.put("message", "newHolidayForm.htm");
		return new ModelAndView("success", model);
	}

	private List<LocalDate> getDateList(String startDate, String endDate) {

		LocalDate start = LocalDate.parse(startDate);
		LocalDate end = LocalDate.parse(endDate);
		List<LocalDate> totalDates = new ArrayList<>();
		while (!start.isAfter(end)) {
			totalDates.add(start);
			start = start.plusDays(1);
		}

		return totalDates;
	}

	@RequestMapping(value = "/deleteHoliday.htm", method = RequestMethod.GET)
	public ModelAndView deleteHoliday(Holiday holidayBean, Principal principal) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}
		commonService.deleteAnObjectById("Holiday", holidayBean.getId());
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("message", "newHolidayForm.htm");
		return new ModelAndView("success", model);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/checkHoliday.htm", method = RequestMethod.POST)
	public @ResponseBody String checkHolidayExists(HttpServletRequest request, HttpServletResponse response,
			Principal principal, HttpSession session) throws Exception {
		Company company = (Company) session.getAttribute("company");
		String text = "true";
		if (request.getParameter("holidayDate") != null) {
			// Holiday holiday =
			// (Holiday)commonService.getAnObjectByAnyUniqueColumn("Holiday", "holidayDate",
			// request.getParameter("holidayDate").toString());
			List<Holiday> holidayList = (List<Holiday>) (Object) commonService.getObjectListByTwoColumn("Holiday",
					"company.id", company.getId().toString(), "holidayDate",
					request.getParameter("holidayDate").toString());
			if (holidayList.size() > 0) {
				text = "false";
			}
		}
		return text;
	}

}