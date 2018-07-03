package com.nazdaq.payrms.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "employee_salary_history")
public class EmployeeSalaryHistory implements Serializable {

	private static final long serialVersionUID = -723583058586873479L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "employee_id", nullable = true)
	private Employee employee;

	// employee details start
	@Column(name = "emp_name", nullable = true)
	private String empName;

	@Column(name = "emp_id", nullable = true)
	private String empId;

	@Column(name = "joining_date", nullable = true)
	private String joiningDate;

	@Column(name = "designation", nullable = true)
	private String designation;

	@Column(name = "department", nullable = true)
	private String department;

	@Column(name = "bank_acc_no", nullable = true)
	private String bankAccNo;
	// employee details end

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "pay_month_id", nullable = true)
	private PaymentMonth paymentMonth;

	@Column(name = "gross_salary", nullable = true)
	private Double grossSalary = 0.0;

	@Column(name = "total_salary", nullable = true)
	private Double totalSalary = 0.0;

	@Column(name = "basic_salary")
	private Double basicSalary = 0.0;

	@Column(name = "medical")
	private Double medical = 0.0;

	@Column(name = "house_rent")
	private Double houseRent = 0.0;

	@Column(name = "conveyance")
	private Double conveyance = 0.0;

	@Column(name = "consalited_salaty")
	private Double consalitedSalary = 0.0;

	@Column(name = "other_salary")
	private Double otherSalary = 0.0;

	@Column(name = "fine_amount")
	private Double fineAmount = 0.0;

	@Column(name = "cash_payment")
	private Double cashPayment = 0.0;

	@Column(name = "bank_payment")
	private Double bankPayment = 0.0;

	@Column(name = "total_payment")
	private Double totalPayment = 0.0;

	@Column(name = "income_tax")
	private Double incomeTax = 0.0;

	@Column(name = "adcence_deduction")
	private Double abcenceDeduction = 0.0;

	@Column(name = "bonus")
	private Double bonus = 0.0;

	@Column(name = "arrear")
	private Double arrear = 0.0;

	@Column(name = "incharge")
	private Double incharge = 0.0;

	@Column(name = "others")
	private Double others = 0.0;

	@Column(name = "overtime")
	private Double overtime = 0.0;

	@Column(name = "stamp")
	private Double stamp = 0.0;

	@Column(name = "total_advance")
	private Double totalAdvance = 0.0;

	@Column(name = "due_amount", nullable = true)
	private Double dueAmount = 0.0;

	@Column(name = "installment")
	private Double installment = 0.0;

	@Column(name = "pf_amount")
	private Double pfAmount = 0.0;

	// working days info
	@Column(name = "working_days")
	private Integer workingDays = 0;

	@Column(name = "absence_days")
	private Integer absenceDays = 0;

	@Column(name = "present_days")
	private Integer presentDays = 0;

	@Column(name = "last_review_amt")
	private Double lastReviewAmt = 0.0;

	@Column(name = "total_earning")
	private Double totalEarning = 0.0;

	@Column(name = "total_deduct")
	private Double totalDeduct = 0.0;

	@Column(name = "net_salary")
	private Double netSalary = 0.0;

	@Transient
	private Integer paymentMonthId;

	@Transient
	private Integer employeeId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(Double basicSalary) {
		this.basicSalary = basicSalary;
	}

	public Double getFineAmount() {
		return fineAmount;
	}

	public void setFineAmount(Double fineAmount) {
		this.fineAmount = fineAmount;
	}

	public Double getCashPayment() {
		return cashPayment;
	}

	public void setCashPayment(Double cashPayment) {
		this.cashPayment = cashPayment;
	}

	public Double getBankPayment() {
		return bankPayment;
	}

	public void setBankPayment(Double bankPayment) {
		this.bankPayment = bankPayment;
	}

	public Double getTotalPayment() {
		return totalPayment;
	}

	public void setTotalPayment(Double totalPayment) {
		this.totalPayment = totalPayment;
	}

	public Double getIncomeTax() {
		return incomeTax;
	}

	public void setIncomeTax(Double incomeTax) {
		this.incomeTax = incomeTax;
	}

	public Double getAbcenceDeduction() {
		return abcenceDeduction;
	}

	public void setAbcenceDeduction(Double abcenceDeduction) {
		this.abcenceDeduction = abcenceDeduction;
	}

	public Double getBonus() {
		return bonus;
	}

	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}

	public Double getArrear() {
		return arrear;
	}

	public void setArrear(Double arrear) {
		this.arrear = arrear;
	}

	public Double getIncharge() {
		return incharge;
	}

	public void setIncharge(Double incharge) {
		this.incharge = incharge;
	}

	public Double getOthers() {
		return others;
	}

	public void setOthers(Double others) {
		this.others = others;
	}

	public Double getStamp() {
		return stamp;
	}

	public void setStamp(Double stamp) {
		this.stamp = stamp;
	}

	public Double getTotalAdvance() {
		return totalAdvance;
	}

	public void setTotalAdvance(Double totalAdvance) {
		this.totalAdvance = totalAdvance;
	}

	public Double getInstallment() {
		return installment;
	}

	public void setInstallment(Double installment) {
		this.installment = installment;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public PaymentMonth getPaymentMonth() {
		return paymentMonth;
	}

	public void setPaymentMonth(PaymentMonth paymentMonth) {
		this.paymentMonth = paymentMonth;
	}

	public Integer getPaymentMonthId() {
		return paymentMonthId;
	}

	public void setPaymentMonthId(Integer paymentMonthId) {
		this.paymentMonthId = paymentMonthId;
	}

	public Double getMedical() {
		return medical;
	}

	public void setMedical(Double medical) {
		this.medical = medical;
	}

	public Double getHouseRent() {
		return houseRent;
	}

	public void setHouseRent(Double houseRent) {
		this.houseRent = houseRent;
	}

	public Double getConveyance() {
		return conveyance;
	}

	public void setConveyance(Double conveyance) {
		this.conveyance = conveyance;
	}

	public Double getConsalitedSalary() {
		return consalitedSalary;
	}

	public void setConsalitedSalary(Double consalitedSalary) {
		this.consalitedSalary = consalitedSalary;
	}

	public Double getOtherSalary() {
		return otherSalary;
	}

	public void setOtherSalary(Double otherSalary) {
		this.otherSalary = otherSalary;
	}

	public Double getOvertime() {
		return overtime;
	}

	public void setOvertime(Double overtime) {
		this.overtime = overtime;
	}

	public Double getGrossSalary() {
		return grossSalary;
	}

	public void setGrossSalary(Double grossSalary) {
		this.grossSalary = grossSalary;
	}

	public Double getPfAmount() {
		return pfAmount;
	}

	public void setPfAmount(Double pfAmount) {
		this.pfAmount = pfAmount;
	}

	public Integer getWorkingDays() {
		return workingDays;
	}

	public void setWorkingDays(Integer workingDays) {
		this.workingDays = workingDays;
	}

	public Integer getAbsenceDays() {
		return absenceDays;
	}

	public void setAbsenceDays(Integer absenceDays) {
		this.absenceDays = absenceDays;
	}

	public Integer getPresentDays() {
		return presentDays;
	}

	public void setPresentDays(Integer presentDays) {
		this.presentDays = presentDays;
	}

	public Double getLastReviewAmt() {
		return lastReviewAmt;
	}

	public void setLastReviewAmt(Double lastReviewAmt) {
		this.lastReviewAmt = lastReviewAmt;
	}

	public Double getTotalSalary() {
		return totalSalary;
	}

	public void setTotalSalary(Double totalSalary) {
		this.totalSalary = totalSalary;
	}

	public Double getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(Double dueAmount) {
		this.dueAmount = dueAmount;
	}

	public Double getTotalEarning() {
		return totalEarning;
	}

	public void setTotalEarning(Double totalEarning) {
		this.totalEarning = totalEarning;
	}

	public Double getTotalDeduct() {
		return totalDeduct;
	}

	public void setTotalDeduct(Double totalDeduct) {
		this.totalDeduct = totalDeduct;
	}

	public Double getNetSalary() {
		return netSalary;
	}

	public void setNetSalary(Double netSalary) {
		this.netSalary = netSalary;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(String joiningDate) {
		this.joiningDate = joiningDate;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getBankAccNo() {
		return bankAccNo;
	}

	public void setBankAccNo(String bankAccNo) {
		this.bankAccNo = bankAccNo;
	}

}
