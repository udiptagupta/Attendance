package com.ntmo.attendance.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table
@IdClass(AttendanceId.class)
public class Attendance {
	
	@Id
	@Column
	private int employeeId;
	@Id
	@Column
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	private Date presenceDate;
	
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public Date getPresenceDate() {
		return presenceDate;
	}
	public void setPresenceDate(Date presenceDate) {
		this.presenceDate = presenceDate;
	}
	@Override
	public String toString() {
		return "Attendance [employeeId=" + employeeId + ", presenceDate=" + presenceDate + "]";
	}
}
