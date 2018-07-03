package com.nazdaq.payrms.model;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "pf_profit")
public class PFProfit implements Serializable {

	private static final long serialVersionUID = -723583058586873479L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "given_date")
	private String givenDate;

	@Column(name = "pf_amount")
	private Double pfAmount = 0.0;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "employee_id", nullable = true)
	private Employee employee;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "pay_month_id", nullable = true)
	private PaymentMonth paymentMonth;

	@Column(name = "created_by", nullable = true)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss a")
	@Column(name = "created_date", nullable = true)
	private Date createdDate;

	@Column(name = "modified_by", nullable = true)
	private String modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss a")
	@Column(name = "modified_date", nullable = true)
	private Date modifiedDate;

	@Column(name = "is_final_submit", nullable = true)
	private String finalSubmit = "0";

	@Column(name = "remarks", nullable = true)
	private String remarks;

	@Transient
	private Integer paymentMonthId;

	@Transient
	private Integer employeeId;

	@Transient
	private Integer recid;

	@Transient
	private Integer slNo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGivenDate() {
		return givenDate;
	}

	public void setGivenDate(String givenDate) {
		this.givenDate = givenDate;
	}

	public Double getPfAmount() {
		return pfAmount;
	}

	public void setPfAmount(Double pfAmount) {
		this.pfAmount = pfAmount;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getFinalSubmit() {
		return finalSubmit;
	}

	public void setFinalSubmit(String finalSubmit) {
		this.finalSubmit = finalSubmit;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getRecid() {
		return recid;
	}

	public void setRecid(Integer recid) {
		this.recid = recid;
	}

	public Integer getSlNo() {
		return slNo;
	}

	public void setSlNo(Integer slNo) {
		this.slNo = slNo;
	}

}
