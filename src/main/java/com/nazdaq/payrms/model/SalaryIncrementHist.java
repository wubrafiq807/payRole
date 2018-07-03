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
@Table(name = "salary_inc_hist")
public class SalaryIncrementHist implements Serializable {

	private static final long serialVersionUID = -723583058586873479L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "employee_id", nullable = true)
	private Employee employee;

	@Column(name = "basic_salary", nullable = false)
	private Double basicSalary = 0.0;

	@Column(name = "gross_salary", nullable = false)
	private Double grossSalary = 0.0;

	@Column(name = "other_salary")
	private Double otherSalary = 0.0;

	@Column(name = "consalited_salaty")
	private Double consalitedSalary = 0.0;

	@Column(name = "medical")
	private Double medical = 0.0;

	@Column(name = "house_rent")
	private Double houseRent = 0.0;
	
	@Column(name = "conveyance")
	private Double conveyance = 0.0;
	
	@Transient
	private Integer employeeId;

	@Column(name = "created_by", nullable = true)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss a")
	@Column(name = "created_date", nullable = true)
	private Date createdDate;

	@Column(name = "remarks")
	private String remarks;

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

	public Double getOtherSalary() {
		return otherSalary;
	}

	public void setOtherSalary(Double otherSalary) {
		this.otherSalary = otherSalary;
	}

	public Double getConsalitedSalary() {
		return consalitedSalary;
	}

	public void setConsalitedSalary(Double consalitedSalary) {
		this.consalitedSalary = consalitedSalary;
	}

	public Double getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(Double basicSalary) {
		this.basicSalary = basicSalary;
	}

	public Double getGrossSalary() {
		return grossSalary;
	}

	public void setGrossSalary(Double grossSalary) {
		this.grossSalary = grossSalary;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
