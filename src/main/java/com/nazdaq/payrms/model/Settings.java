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
@Table(name = "settings")
public class Settings implements Serializable {

	private static final long serialVersionUID = -723583058586873479L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "working_day")
	private String workingDay;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "company_id", nullable = true)
	private Company company;

	@Transient
	private Integer companyId;

	@Column(name = "bonus_month1")
	private String bonusMonth1;

	@Column(name = "bonus_month")
	private String bonusMonth2;

	@Column(name = "bonus_year")
	private String bonusYear;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getWorkingDay() {
		return workingDay;
	}

	public void setWorkingDay(String workingDay) {
		this.workingDay = workingDay;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getBonusMonth1() {
		return bonusMonth1;
	}

	public void setBonusMonth1(String bonusMonth1) {
		this.bonusMonth1 = bonusMonth1;
	}

	public String getBonusMonth2() {
		return bonusMonth2;
	}

	public void setBonusMonth2(String bonusMonth2) {
		this.bonusMonth2 = bonusMonth2;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getBonusYear() {
		return bonusYear;
	}

	public void setBonusYear(String bonusYear) {
		this.bonusYear = bonusYear;
	}

}
