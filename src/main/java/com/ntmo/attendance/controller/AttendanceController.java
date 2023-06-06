package com.ntmo.attendance.controller;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ntmo.attendance.entity.Attendance;
import com.ntmo.attendance.entity.Employee;
import com.ntmo.attendance.entity.EmployeeAttendance;
import com.ntmo.attendance.repos.AttendanceRepository;
import com.ntmo.attendance.service.EmployeeService;

@RestController
public class AttendanceController {

	@Autowired
	private EmployeeService empService;
	
	@Autowired 
	private AttendanceRepository aRep;
	
	@PostMapping("/attendance")
	public Attendance save(@RequestBody Attendance att) {
		Attendance newAtt = aRep.save(att);
		return newAtt;
	}

	// Add attendance for employee for current date
	@SuppressWarnings("deprecation")
	@PostMapping("/attendance/{id}")
	public Attendance save(@PathVariable int id) {
		Attendance att = new Attendance();
		att.setEmployeeId(id);
		Date cDate = new Date();
		cDate.setHours(2);
		cDate.setMinutes(0);
		cDate.setSeconds(0);
		att.setPresenceDate(cDate);
		Attendance retVal = save(att);
		return retVal;
	}
	
	// Find attendance details of employee for current month
	@GetMapping("/attendance/{id}")
	public EmployeeAttendance getEmployeeAttendance(@PathVariable int id) {
		// return attendanceService.getEmployeeAttendance(id);
		Date startDate = Date.from(LocalDate.now().withDayOfMonth(1).atStartOfDay().toInstant(ZoneOffset.UTC));
		// Date startDate = Date.from(LocalDate.now().minusMonths(1).withDayOfMonth(1).atStartOfDay().toInstant(ZoneOffset.UTC));		// For previous month
		Date endDate = new Date();
		List<Attendance> aList = aRep.findByEmployeeIdAndPresenceDateBetween(id, startDate, endDate);
		
		EmployeeAttendance ea = new EmployeeAttendance();
		ea.setEmployeeId(id);
		List<Date> list = new ArrayList<Date>();
		
		for(Attendance att: aList) {
			list.add(att.getPresenceDate());
		}
		
		ea.setPresenceDate(list);
		return ea;
	}
	
	@GetMapping("/attendanceManagedBy/{id}")
	public List<EmployeeAttendance> getAttendanceManagedBy(@PathVariable int id) {
		List<EmployeeAttendance> eaList = new ArrayList<EmployeeAttendance>();
		List<Employee> empList = empService.getEmployee();
		List<Employee> filteredList = empList
				.stream()
				.filter( emp -> emp.getManagerId() == id)
				.collect(Collectors.toList());
		for(Employee emp : filteredList) {
			EmployeeAttendance ea = getEmployeeAttendance(emp.getId());
			if(ea != null) {
				eaList.add(ea);
			}
		}
		
		return eaList;
	}

}
