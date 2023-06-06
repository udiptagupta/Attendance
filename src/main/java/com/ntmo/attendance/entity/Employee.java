package com.ntmo.attendance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Employee {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column
	private int id;
	@Column(length = 45)
	private String employeeName;
	@Column(length = 45)
	private String employeeMail;
	@Column
	private int locationId;
	@Column
	private int managerId;
	@Column(length = 200)
	private String remarks;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	
	public String getEmployeeMail() {
		return employeeMail;
	}
	public void setEmployeeMail(String employeeMail) {
		this.employeeMail = employeeMail;
	}
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	public int getManagerId() {
		return managerId;
	}
	public void setManagerId(int managerId) {
		this.managerId = managerId;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemark(String remarks) {
		this.remarks = remarks;
	}
	
}
