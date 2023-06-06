package com.ntmo.attendance.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class AttendanceId implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int employeeId;
	
	private Date presenceDate;

	public AttendanceId() {
    }
	
	public AttendanceId(int employeeId, Date presenceDate) {
		super();
		this.employeeId = employeeId;
		this.presenceDate = presenceDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(employeeId, presenceDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AttendanceId other = (AttendanceId) obj;
		return employeeId == other.employeeId && Objects.equals(presenceDate, other.presenceDate);
	}
	
}
