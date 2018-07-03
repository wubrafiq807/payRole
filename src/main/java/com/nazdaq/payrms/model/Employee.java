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
@Table(name = "employee")
public class Employee implements Serializable {

	private static final long serialVersionUID = -723583058586873479L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	@Column(name = "employee_id", length = 11, nullable = false)
	private String employeeId;

	@Column(name = "email", length = 100, nullable = false)
	private String emailAddress;

	@Column(name = "father_name", length = 100, nullable = true)
	private String fatherName;

	@Column(name = "mother_name", length = 100, nullable = true)
	private String motherName;

	@Column(name = "date_of_birth", length = 100, nullable = true)
	private String dateOfBirth;

	@Column(name = "full_name", length = 100, nullable = true)
	private String fullname;

	@Column(name = "personal_phone", length = 20, nullable = false)
	private String personalPhone;

	@Column(name = "work_phone", length = 20, nullable = true)
	private String workPhone;

	@Column(name = "present_address", length = 256, nullable = true)
	private String presentAddress;

	@Column(name = "permanent_address", length = 256, nullable = true)
	private String permanentAddress;

	@Column(name = "gender", length = 20, nullable = false)
	private String gender;

	@Column(name = "joining_date", length = 256, nullable = true)
	private String joiningDate;

	@Column(name = "bank_acc_no", length = 256, nullable = true)
	private String bankAccNo;

	@Column(name = "status", length = 1, nullable = true)
	private int status;

	@Column(name = "merital_status", length = 20, nullable = true)
	private String meritalStatus;
	@Column(name = "logo", nullable = true)
	private String logo;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "blood_group_id", nullable = true)
	private BloodGroup bloodGroup;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "company_id", nullable = true)
	private Company company;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "hom_district_id", nullable = true)
	private District district;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "department_id", nullable = true)
	private Department department;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "designation_id", nullable = true)
	private Designation designation;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "religion_id", nullable = true)
	private Religion religion;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "pay_site_id", nullable = true)
	private PaySite paySite;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "work_location_id", nullable = true)
	private WorkLocation workLocation;

	@Transient
	private Integer districtId;

	@Transient
	private Integer companyId;

	@Transient
	private Integer departmentId;

	@Transient
	private Integer designationId;

	@Transient
	private Integer paySiteId;

	@Transient
	private Integer workLocationId;

	@Transient
	private Integer religionId;

	@Transient
	private Integer bloodGroupId;

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

	@Column(name = "remarks", nullable = true)
	private String remarks;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getMotherName() {
		return motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getPersonalPhone() {
		return personalPhone;
	}

	public void setPersonalPhone(String personalPhone) {
		this.personalPhone = personalPhone;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getPresentAddress() {
		return presentAddress;
	}

	public void setPresentAddress(String presentAddress) {
		this.presentAddress = presentAddress;
	}

	public String getPermanentAddress() {
		return permanentAddress;
	}

	public void setPermanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMeritalStatus() {
		return meritalStatus;
	}

	public void setMeritalStatus(String meritalStatus) {
		this.meritalStatus = meritalStatus;
	}

	public BloodGroup getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(BloodGroup bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public Integer getBloodGroupId() {
		return bloodGroupId;
	}

	public void setBloodGroupId(Integer bloodGroupId) {
		this.bloodGroupId = bloodGroupId;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Designation getDesignation() {
		return designation;
	}

	public void setDesignation(Designation designation) {
		this.designation = designation;
	}

	public PaySite getPaySite() {
		return paySite;
	}

	public void setPaySite(PaySite paySite) {
		this.paySite = paySite;
	}

	public WorkLocation getWorkLocation() {
		return workLocation;
	}

	public void setWorkLocation(WorkLocation workLocation) {
		this.workLocation = workLocation;
	}

	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getDesignationId() {
		return designationId;
	}

	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}

	public Integer getPaySiteId() {
		return paySiteId;
	}

	public void setPaySiteId(Integer paySiteId) {
		this.paySiteId = paySiteId;
	}

	public Integer getWorkLocationId() {
		return workLocationId;
	}

	public void setWorkLocationId(Integer workLocationId) {
		this.workLocationId = workLocationId;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Religion getReligion() {
		return religion;
	}

	public void setReligion(Religion religion) {
		this.religion = religion;
	}

	public Integer getReligionId() {
		return religionId;
	}

	public void setReligionId(Integer religionId) {
		this.religionId = religionId;
	}

	public String getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(String joiningDate) {
		this.joiningDate = joiningDate;
	}

	public String getBankAccNo() {
		return bankAccNo;
	}

	public void setBankAccNo(String bankAccNo) {
		this.bankAccNo = bankAccNo;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", employeeId=" + employeeId + ", emailAddress=" + emailAddress + ", fatherName="
				+ fatherName + ", motherName=" + motherName + ", dateOfBirth=" + dateOfBirth + ", fullname=" + fullname
				+ ", personalPhone=" + personalPhone + ", workPhone=" + workPhone + ", presentAddress=" + presentAddress
				+ ", permanentAddress=" + permanentAddress + ", gender=" + gender + ", status=" + status
				+ ", districtId=" + districtId + ", meritalStatus=" + meritalStatus + ", companyId=" + companyId
				+ ", bloodGroup=" + bloodGroup + ", paySite=" + paySite + "]";
	}

}
