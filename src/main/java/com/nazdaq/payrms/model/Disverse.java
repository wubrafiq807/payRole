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
@Table(name = "disverse")
public class Disverse implements Serializable {

	private static final long serialVersionUID = -723583058586873479L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "disverse_amount", nullable = false)
	private Double disverseAmount = 0.0;

	@Column(name = "disverse_interest", nullable = false)
	private Double disverseInterest = 0.0;

	@Column(name = "due_amount", nullable = true)
	private Double dueAmount = 0.0;

	@Column(name = "last_deduct_from_due", nullable = true)
	private Double lastDeductFromDue = 0.0;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "dirverse_date", nullable = true)
	private Date dirverseDate;

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

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "employee_id", nullable = true)
	private Employee employee;

	@Column(name = "remarks", nullable = true)
	private String remarks;

	@Transient
	private Integer employeeId;

	@Transient
	private String disburseDateBean;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getDisverseAmount() {
		return disverseAmount;
	}

	public void setDisverseAmount(Double disverseAmount) {
		this.disverseAmount = disverseAmount;
	}

	public Double getDisverseInterest() {
		return disverseInterest;
	}

	public void setDisverseInterest(Double disverseInterest) {
		this.disverseInterest = disverseInterest;
	}

	public Double getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(Double dueAmount) {
		this.dueAmount = dueAmount;
	}

	public Date getDirverseDate() {
		return dirverseDate;
	}

	public void setDirverseDate(Date dirverseDate) {
		this.dirverseDate = dirverseDate;
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

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getDisburseDateBean() {
		return disburseDateBean;
	}

	public void setDisburseDateBean(String disburseDateBean) {
		this.disburseDateBean = disburseDateBean;
	}

	public Double getLastDeductFromDue() {
		return lastDeductFromDue;
	}

	public void setLastDeductFromDue(Double lastDeductFromDue) {
		this.lastDeductFromDue = lastDeductFromDue;
	}

}
