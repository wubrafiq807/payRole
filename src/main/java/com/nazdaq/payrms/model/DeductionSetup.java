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
@Table(name = "deduct_setup")
public class DeductionSetup implements Serializable {

	private static final long serialVersionUID = -723583058586873479L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "deduct_type")
	private String deductType;

	@Column(name = "deduct_unit")
	private String deductUnit;

	@Column(name = "deduct_amount")
	private Double deductAmount;

	@Column(name = "deduct_minimum")
	private Double deductMinimum;

	@Column(name = "deduct_maximum")
	private String deductMaximum;

	@Column(name = "deduct_status")
	private Integer status;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "company_id", nullable = true)
	private Company company;

	@Transient
	private Integer companyId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeductType() {
		return deductType;
	}

	public void setDeductType(String deductType) {
		this.deductType = deductType;
	}

	public String getDeductUnit() {
		return deductUnit;
	}

	public void setDeductUnit(String deductUnit) {
		this.deductUnit = deductUnit;
	}

	public Double getDeductAmount() {
		return deductAmount;
	}

	public void setDeductAmount(Double deductAmount) {
		this.deductAmount = deductAmount;
	}

	public Double getDeductMinimum() {
		return deductMinimum;
	}

	public void setDeductMinimum(Double deductMinimum) {
		this.deductMinimum = deductMinimum;
	}

	public String getDeductMaximum() {
		return deductMaximum;
	}

	public void setDeductMaximum(String deductMaximum) {
		this.deductMaximum = deductMaximum;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

}
