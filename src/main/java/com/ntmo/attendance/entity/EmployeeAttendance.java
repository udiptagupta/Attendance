package com.ntmo.attendance.entity;

import java.util.Date;
import java.util.List;

public class EmployeeAttendance {
	
	private int employeeId;
	private String employeeName;
	private List<Date> presenceDate;
	private String remarks;
	
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public List<Date> getPresenceDate() {
		return presenceDate;
	}
	public void setPresenceDate(List<Date> presenceDate) {
		this.presenceDate = presenceDate;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@Override
	public String toString() {
		return "EmployeeAttendance [employeeId=" + employeeId + ", employeeName=" + employeeName + ", presenceDate="
				+ presenceDate + ", remarks=" + remarks + "]";
	}
	
}
