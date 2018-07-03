package com.nazdaq.payrms.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.nazdaq.payrms.model.AbsenceDeduction;
import com.nazdaq.payrms.model.AdditionalAllowance;
import com.nazdaq.payrms.model.AdditionalDeduction;
import com.nazdaq.payrms.model.AdditionalSalary;
import com.nazdaq.payrms.model.Advance;
import com.nazdaq.payrms.model.CashPaymentEmployee;
import com.nazdaq.payrms.model.Company;
import com.nazdaq.payrms.model.Disverse;
import com.nazdaq.payrms.model.Employee;
import com.nazdaq.payrms.model.EmployeeSalaryHistory;
import com.nazdaq.payrms.model.FixedIncomeTax;
import com.nazdaq.payrms.model.Holiday;
import com.nazdaq.payrms.model.PFProfit;
import com.nazdaq.payrms.model.PaymentMonth;
import com.nazdaq.payrms.model.Salary;
import com.nazdaq.payrms.model.Settings;
import com.nazdaq.payrms.service.CommonService;
import com.nazdaq.payrms.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class SalaryProcessConrtroller implements Constants{
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${cc.email.addresss}")
	String ccEmailAddresss;
	
	@Value("${common.email.address}")
	String commonEmailAddress;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/empSalHistProcessForm.htm", method = RequestMethod.GET)
	public ModelAndView empSalHistProcessForm (@ModelAttribute("salaryProcessForm") EmployeeSalaryHistory empSalHistBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		List<PaymentMonth> paymentMonthList = (List<PaymentMonth>)(Object)
				commonService.getAllObjectList("PaymentMonth");		
		
			Collections.reverse(paymentMonthList);
		
		Map <String, Object> model = new HashMap<String, Object>();	
		
		model.put("paymentMonthList", paymentMonthList);	
		return new ModelAndView("empSalHistProcessForm", model);		
	}
	
	//salaryProcess.htm
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/salaryProcess.htm", method = RequestMethod.POST)
	public ModelAndView salaryProcess (@ModelAttribute("salaryProcessForm") EmployeeSalaryHistory empSalHistBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Company company = (Company) session.getAttribute("company");		
		PaymentMonth payMonth = (PaymentMonth) commonService.getAnObjectByAnyUniqueColumn("PaymentMonth", "id", empSalHistBean.getPaymentMonthId().toString());		
		// process start from here
		EmployeeSalaryHistory salHist = null;
		//List<EmployeeSalaryHistory> salList = (List<EmployeeSalaryHistory>)(Object) commonService.getObjectListByAnyColumn("EmployeeSalaryHistory", "employee.company.id", company.getId().toString());
		
		List<EmployeeSalaryHistory> salList = (List<EmployeeSalaryHistory>)(Object)
				commonService.getObjectListByTwoColumn("EmployeeSalaryHistory", "employee.company.id", company.getId().toString(), "paymentMonth.id", payMonth.getId().toString());
		if(salList.size() > 0) {
			return new ModelAndView("redirect:/salaryProcessList.htm?paymentMonthId="+empSalHistBean.getPaymentMonthId().toString());	
		} else {
			
			
			List<Employee> employeeList = (List<Employee>)(Object)
					commonService.getObjectListByTwoColumn("Employee", "company.id", company.getId().toString(), "status", "1");
			List<EmployeeSalaryHistory> salHistList = new ArrayList<EmployeeSalaryHistory>();
			
			for (Employee employee : employeeList) {
				salHist = new EmployeeSalaryHistory();
				salHist.setEmployee(employee);
				salHist.setEmpId(employee.getEmployeeId());
				salHist.setEmpName(employee.getFullname());
				salHist.setDepartment(employee.getDepartment().getName());
				salHist.setDesignation(employee.getDesignation().getName());
				salHist.setJoiningDate(employee.getJoiningDate());
				salHist.setBankAccNo(employee.getBankAccNo());
				salHist.setPaymentMonth(payMonth);
				salHistList.add(salHist);
			}			
			
			List<Salary> salaryList = (List<Salary>)(Object)
					commonService.getObjectListByTwoColumn("Salary", "employee.company.id", company.getId().toString(), "employee.status", "1");		
			
			
			List<Advance> advanceList = (List<Advance>)(Object)
					commonService.getObjectListByTwoColumn("Advance", "employee.company.id", company.getId().toString(), "paymentMonth.id", empSalHistBean.getPaymentMonthId().toString());
			
			List<AbsenceDeduction> absenceList = (List<AbsenceDeduction>)(Object)
					commonService.getObjectListByTwoColumn("AbsenceDeduction", "employee.company.id", company.getId().toString(), "paymentMonth.id", empSalHistBean.getPaymentMonthId().toString());
			
			List<AdditionalDeduction> additionalDeductionList = (List<AdditionalDeduction>)(Object)
					commonService.getObjectListByTwoColumn("AdditionalDeduction", "employee.company.id", company.getId().toString(), "paymentMonth.id", empSalHistBean.getPaymentMonthId().toString());
									
			List<AdditionalSalary> additionalSalaryList = (List<AdditionalSalary>)(Object)
					commonService.getObjectListByTwoColumn("AdditionalSalary", "employee.company.id", company.getId().toString(), "paymentMonth.id", empSalHistBean.getPaymentMonthId().toString());

			List<AdditionalAllowance> additionAllowList = (List<AdditionalAllowance>)(Object)
					commonService.getObjectListByTwoColumn("AdditionalAllowance", "employee.company.id", company.getId().toString(), "paymentMonth.id", empSalHistBean.getPaymentMonthId().toString());
						
			List<CashPaymentEmployee> cashPayList = (List<CashPaymentEmployee>)(Object)
					commonService.getObjectListByTwoColumn("CashPaymentEmployee", "employee.company.id", company.getId().toString(), "paymentMonth.id", empSalHistBean.getPaymentMonthId().toString());
			
			List<FixedIncomeTax> fixedIncomeTaxList = (List<FixedIncomeTax>)(Object)
					commonService.getObjectListByTwoColumn("FixedIncomeTax", "employee.company.id", company.getId().toString(), "employee.status", "1");
			
			List<PFProfit> pfProfitList = (List<PFProfit>)(Object)
					commonService.getObjectListByTwoColumn("PFProfit", "employee.company.id", company.getId().toString(), "paymentMonth.id", empSalHistBean.getPaymentMonthId().toString());
			
			Integer workingDays = this.getDaysOfMonth(payMonth.getPaymentMonthName(), Integer.parseInt(payMonth.getPayYear()));				
			String holidayDate = payMonth.getPayYear()+"_"+this.getMonthPossition(payMonth.getPaymentMonthName());
			List<Holiday> holidayList = (List<Holiday>)(Object)commonService.getObjectListByTwoColumnOneFixedAndOneLikeOrderByAnyColumn("Holiday", "company.id", company.getId().toString(), "holidayDate", holidayDate, "id", "DESC");
			
			//bonus
			List<Settings> settingList = (List<Settings>) (Object) commonService.getObjectListByTwoColumn("Settings",
					"company.id", company.getId().toString(), "bonusYear", payMonth.getPayYear().toString() );
			Settings settings = null;
			if(settingList != null && settingList.size() > 0) {
				settings = settingList.get(0);
			}
												
			for (EmployeeSalaryHistory esh : salHistList) {
				esh.setWorkingDays(workingDays - holidayList.size());				
				for (Salary salary : salaryList) {
					if(esh.getEmployee().getId().toString().equals(salary.getEmployee().getId().toString())) {
						esh.setBasicSalary(salary.getBasicSalary());
						esh.setOtherSalary(salary.getOtherSalary());
						esh.setMedical(salary.getMedical());
						esh.setHouseRent(salary.getHouseRent());
						esh.setConveyance(salary.getConveyance());
						esh.setConsalitedSalary(salary.getConsalitedSalary());
						esh.setGrossSalary(salary.getGrossSalary());
						if(salary.getLastReviewAmt() != null) {
							esh.setLastReviewAmt(salary.getLastReviewAmt());
						} else {
							esh.setLastReviewAmt(0.0);
						}
												
						if(salary.getConsalitedSalary() > 0) {
							esh.setTotalSalary(salary.getConsalitedSalary());
						}else {
							esh.setTotalSalary(salary.getGrossSalary());
						}
						break;
					}
				}
				
				for (Advance adv : advanceList) {
					if(esh.getEmployee().getId().toString().equals(adv.getEmployee().getId().toString())) {
						esh.setInstallment(adv.getAdvAmount());	
						break;
					}
				}
				
				for (AbsenceDeduction ad : absenceList) {
					if(esh.getEmployee().getId().toString().equals(ad.getEmployee().getId().toString())) {
						esh.setAbcenceDeduction(ad.getDeductAmount());	
						esh.setAbsenceDays(ad.getDeductDays());
						esh.setPresentDays(esh.getWorkingDays() - ad.getDeductDays());
						break;
					}
				}
				
				for (AdditionalDeduction ad : additionalDeductionList) {
					if(esh.getEmployee().getId().toString().equals(ad.getEmployee().getId().toString())) {
						esh.setStamp(ad.getDeductStamp());
						esh.setFineAmount(ad.getFineAmount());
						break;
					}
				}
				
				for (AdditionalSalary as : additionalSalaryList) {
					if(esh.getEmployee().getId().toString().equals(as.getEmployee().getId().toString())) {
						esh.setConsalitedSalary(as.getConsolitedSalary());		
						esh.setOtherSalary(as.getOthersSalary());
						break;
					}
				}
				
				for (AdditionalAllowance aa : additionAllowList) {
					if(esh.getEmployee().getId().toString().equals(aa.getEmployee().getId().toString())) {
						esh.setArrear(aa.getAllowArear());
						esh.setIncharge(aa.getInchargeAllow());
						esh.setOthers(aa.getAllowOthers());
						esh.setOvertime(aa.getOvertime());
						break;
					}
				}
				
				for (CashPaymentEmployee cpe : cashPayList) {
					if(esh.getEmployee().getId().toString().equals(cpe.getEmployee().getId().toString())) {						
						esh.setCashPayment(cpe.getPaymentAmount());
						break;
					}
				}
				
				for (FixedIncomeTax fit : fixedIncomeTaxList) {
					if(esh.getEmployee().getId().toString().equals(fit.getEmployee().getId().toString())) {							
						esh.setIncomeTax(fit.getTaxAmount());
						break;
					}
				}
				
				for (PFProfit pf : pfProfitList) {
					if(esh.getEmployee().getId().toString().equals(pf.getEmployee().getId().toString())) {						
						esh.setPfAmount(pf.getPfAmount());
						break;
					}
				}
				
				List<Disverse> diList = (List<Disverse>) (Object) commonService.getObjectListByAnyColumn("Disverse",
						"employee.id", esh.getEmployee().getId().toString());
				if(diList != null && diList.size() > 0) {
					Collections.reverse(diList);
					Disverse disverse = diList.get(0); 
					esh.setTotalAdvance((disverse.getDisverseAmount()
							+ (disverse.getDisverseAmount() * disverse.getDisverseInterest() / 100)));
					esh.setDueAmount(disverse.getDueAmount());
				}
				
				// 100 % of gross
				if(settings != null) {
					if(settings.getBonusMonth1().equals(payMonth.getPaymentMonthName()) || settings.getBonusMonth2().equals(payMonth.getPaymentMonthName())) {
						esh.setBonus(esh.getGrossSalary());
					}
				}
				
				//payment-mode
				esh.setTotalEarning(esh.getTotalSalary()+esh.getArrear()+esh.getOvertime()+esh.getIncharge()+esh.getOthers());
				esh.setTotalDeduct(esh.getAbcenceDeduction()+esh.getFineAmount()+esh.getPfAmount()+esh.getIncomeTax()+esh.getStamp()+esh.getInstallment());
				esh.setNetSalary(esh.getTotalEarning()-esh.getTotalDeduct());
				esh.setBankPayment(esh.getNetSalary());				
				esh.setTotalPayment(esh.getBankPayment()+esh.getCashPayment());
				
			}
			
			for (EmployeeSalaryHistory esh : salHistList) {
				commonService.saveOrUpdateModelObjectToDB(esh);
			}
			
		}
		// salary process end
		
		return new ModelAndView("redirect:/salaryProcessList.htm?paymentMonthId="+empSalHistBean.getPaymentMonthId().toString());		
	}
	
	@RequestMapping(value = "/salaryProcessList.htm", method = RequestMethod.GET)
	public ModelAndView salaryProcessList (@ModelAttribute("salaryProcessForm") EmployeeSalaryHistory empSalHistBean, Principal principal, HttpSession session) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Company company = (Company) session.getAttribute("company");		
		
		// process start from here
		PaymentMonth payMonth = (PaymentMonth) commonService.getAnObjectByAnyUniqueColumn("PaymentMonth", "id", empSalHistBean.getPaymentMonthId().toString());
		
		//List<EmployeeSalaryHistory> salaryList = (List<EmployeeSalaryHistory>)(Object) commonService.getObjectListByAnyColumn("EmployeeSalaryHistory", "employee.company.id", company.getId().toString());
		List<EmployeeSalaryHistory> salaryList = (List<EmployeeSalaryHistory>)(Object)
				commonService.getObjectListByTwoColumn("EmployeeSalaryHistory", "employee.company.id", company.getId().toString(), "paymentMonth.id", payMonth.getId().toString());
		
		List<PaymentMonth> paymentMonthList = (List<PaymentMonth>)(Object)
				commonService.getAllObjectList("PaymentMonth");		
		
			Collections.reverse(paymentMonthList);
		
		Map <String, Object> model = new HashMap<String, Object>();	
		
		model.put("paymentMonthList", paymentMonthList);
		model.put("salaryList", salaryList);
		model.put("payMonth", payMonth);
		return new ModelAndView("empSalHistProcessForm", model);		
	}
	
	private Integer getDaysOfMonth(String monthName, Integer year) {
		Integer monthDays = 0;
		if(monthName.equals(JANUARY)) {
			monthDays =  31;
		} else if(monthName.equals(FEBRUARY)) {
			if(year % 4 == 0) {
				monthDays =  29;
			} else {
				monthDays =  28;
			}
			
		} else if(monthName.equals(MARCH)) {			
			monthDays =  31;
		} else if(monthName.equals(APRIL)) {
			monthDays =  30;
		} else if(monthName.equals(MAY)) {
			monthDays =  31;
		} else if(monthName.equals(JUNE)) {
			monthDays =  30;
		} else if(monthName.equals(JULY)) {
			monthDays =  31;
		} else if(monthName.equals(AUGUST)) {
			monthDays =  31;
		} else if(monthName.equals(SEPTEMBER)) {
			monthDays =  30;
		} else if(monthName.equals(OCTOBER)) {
			monthDays =  31;
		} else if(monthName.equals(NOVEMBER)) {
			monthDays =  30;
		} else if(monthName.equals(DECEMBER)) {
			monthDays =  31;
		}
		return monthDays;
	}	
	
	private String getMonthPossition(String monthName) {
		String monthPos = "";
		if(monthName.equals(JANUARY)) {
			monthPos =  "01";
		} else if(monthName.equals(FEBRUARY)) {
			monthPos =  "02";			
		} else if(monthName.equals(MARCH)) {			
			monthPos =  "03";
		} else if(monthName.equals(APRIL)) {
			monthPos =  "04";
		} else if(monthName.equals(MAY)) {
			monthPos =  "05";
		} else if(monthName.equals(JUNE)) {
			monthPos =  "06";
		} else if(monthName.equals(JULY)) {
			monthPos =  "07";
		} else if(monthName.equals(AUGUST)) {
			monthPos =  "08";
		} else if(monthName.equals(SEPTEMBER)) {
			monthPos =  "09";
		} else if(monthName.equals(OCTOBER)) {
			monthPos =  "10";
		} else if(monthName.equals(NOVEMBER)) {
			monthPos =  "11";
		} else if(monthName.equals(DECEMBER)) {
			monthPos =  "12";
		}
		return monthPos;
	}
}
