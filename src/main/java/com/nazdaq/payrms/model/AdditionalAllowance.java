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
@Table(name = "additional_allowance")
public class AdditionalAllowance implements Serializable {

	private static final long serialVersionUID = -723583058586873479L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "allow_arear")
	private Double allowArear = 0.0;

	@Column(name = "overtime")
	private Double overtime = 0.0;

	@Column(name = "incharge_allow")
	private Double inchargeAllow = 0.0;

	@Column(name = "allow_others")
	private Double allowOthers = 0.0;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "employee_id", nullable = true)
	private Employee employee;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "payment_month_id", nullable = true)
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
	private Integer employeeId;

	@Transient
	private Integer paymentMonthId;

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

	public Double getAllowArear() {
		return allowArear;
	}

	public void setAllowArear(Double allowArear) {
		this.allowArear = allowArear;
	}

	public Double getOvertime() {
		return overtime;
	}

	public void setOvertime(Double overtime) {
		this.overtime = overtime;
	}

	public Double getInchargeAllow() {
		return inchargeAllow;
	}

	public void setInchargeAllow(Double inchargeAllow) {
		this.inchargeAllow = inchargeAllow;
	}

	public Double getAllowOthers() {
		return allowOthers;
	}

	public void setAllowOthers(Double allowOthers) {
		this.allowOthers = allowOthers;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public PaymentMonth getPaymentMonth() {
		return paymentMonth;
	}

	public void setPaymentMonth(PaymentMonth paymentMonth) {
		this.paymentMonth = paymentMonth;
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

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public Integer getPaymentMonthId() {
		return paymentMonthId;
	}

	public void setPaymentMonthId(Integer paymentMonthId) {
		this.paymentMonthId = paymentMonthId;
	}

	public String getFinalSubmit() {
		return finalSubmit;
	}

	public void setFinalSubmit(String finalSubmit) {
		this.finalSubmit = finalSubmit;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
