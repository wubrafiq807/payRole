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

@Entity
@Table(name = "deduction_employee")
public class DeductionEmployee implements Serializable {

	private static final long serialVersionUID = -723583058586873479L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "employee_id", nullable = true)
	private Employee employee;

	@Transient
	private Integer employeeId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fine_date_time")
	private Date fineDateTime;

	@Column(name = "fine_amount")
	private Double fineAmount;

	@Column(name = "reason")
	private String reason;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public Date getFineDateTime() {
		return fineDateTime;
	}

	public void setFineDateTime(Date fineDateTime) {
		this.fineDateTime = fineDateTime;
	}

	public Double getFineAmount() {
		return fineAmount;
	}

	public void setFineAmount(Double fineAmount) {
		this.fineAmount = fineAmount;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

}
