package com.nazdaq.payrms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "payment_month")
public class PaymentMonth implements Serializable {

	private static final long serialVersionUID = -723583058586873479L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "payment_month_name")
	private String paymentMonthName;

	@Column(name = "pay_name")
	private String payName;

	@Column(name = "pay_year")
	private String payYear;

	@Column(name = "remarks")
	private String remarks;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPaymentMonthName() {
		return paymentMonthName;
	}

	public void setPaymentMonthName(String paymentMonthName) {
		this.paymentMonthName = paymentMonthName;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public String getPayYear() {
		return payYear;
	}

	public void setPayYear(String payYear) {
		this.payYear = payYear;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
