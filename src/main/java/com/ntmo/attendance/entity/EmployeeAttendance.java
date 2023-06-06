package com.ntmo.attendance.entity;

import java.util.Date;
import java.util.List;

public class EmployeeAttendance {
	
	private int employeeId;
	private List<Date> presenceDate;
	
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
	@Override
	public String toString() {
		return "EmployeeAttendance [employeeId=" + employeeId + ", presenceDate=" + presenceDate + "]";
	}

}
