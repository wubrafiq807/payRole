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
@Table(name = "allow_setup")
public class AllowanceSetup implements Serializable {

	private static final long serialVersionUID = -723583058586873479L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "allow_unit")
	private String allowUnit;

	@Column(name = "allow_amount")
	private Double allowAmount;

	@Column(name = "allow_minimum")
	private Double allowMinimum;

	@Column(name = "allow_maximum")
	private String allowMaximum;

	@Column(name = "allow_status")
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

	public String getAllowUnit() {
		return allowUnit;
	}

	public void setAllowUnit(String allowUnit) {
		this.allowUnit = allowUnit;
	}

	public Double getAllowAmount() {
		return allowAmount;
	}

	public void setAllowAmount(Double allowAmount) {
		this.allowAmount = allowAmount;
	}

	public Double getAllowMinimum() {
		return allowMinimum;
	}

	public void setAllowMinimum(Double allowMinimum) {
		this.allowMinimum = allowMinimum;
	}

	public String getAllowMaximum() {
		return allowMaximum;
	}

	public void setAllowMaximum(String allowMaximum) {
		this.allowMaximum = allowMaximum;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
