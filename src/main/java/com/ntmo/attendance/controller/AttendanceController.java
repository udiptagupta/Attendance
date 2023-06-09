package com.ntmo.attendance.controller;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ntmo.attendance.entity.Attendance;
import com.ntmo.attendance.entity.Employee;
import com.ntmo.attendance.entity.EmployeeAttendance;
import com.ntmo.attendance.repos.AttendanceRepository;
import com.ntmo.attendance.service.EmployeeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class AttendanceController {

	@Autowired
	private EmployeeService empService;
	
	@Autowired 
	private AttendanceRepository aRep;
	
	@RequestMapping(value="/attendance", method=RequestMethod.POST)
	public Attendance save(@RequestBody Attendance att) {
		log.debug("AttendanceController:save - " + att);
		Attendance newAtt = aRep.save(att);
		return newAtt;
	}

	// Add attendance for employee for current date
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/attendance/{id}", method=RequestMethod.POST)
	public Attendance save(@PathVariable int id) {
		log.debug("AttendanceController:save");
		
		Date today = new Date();
		EmployeeAttendance ea = getEmployeeAttendance(id);
		List<Date> lDate = ea.getPresenceDate();
		boolean isPresent = false;
		
		for(Date d : lDate) {
			if(d.getDate() == today.getDate() && d.getMonth() == today.getMonth()) {
				isPresent = true;
				break;
			}
		}
		if(!isPresent) {
			Attendance att = new Attendance();
			att.setEmployeeId(id);
			Date cDate = new Date();
			att.setPresenceDate(cDate);
			Attendance retVal = save(att);
			return retVal;
		}
		return null;
	}
	
		// Clear attendance for employee for current date
		@SuppressWarnings("deprecation")
		@RequestMapping(value="/attendance/{id}", method=RequestMethod.DELETE)
		public void delete(@PathVariable int id) {
			log.debug("AttendanceController:delete");
			Date startDate = Date.from(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC));
			// Date startDate = Date.from(LocalDate.now().minusMonths(1).withDayOfMonth(1).atStartOfDay().toInstant(ZoneOffset.UTC));		// For previous month
			Date endDate = new Date();
			endDate.setHours(23);
			endDate.setMinutes(59);
			endDate.setSeconds(59);
			List<Attendance> aList = aRep.findByEmployeeIdAndPresenceDateBetween(id, startDate, endDate);
			
			if(aList != null && aList.size() > 0) {
				Attendance att = new Attendance();
				att.setEmployeeId(id);
				att.setPresenceDate(aList.get(0).getPresenceDate());
				
				aRep.delete(att);
			}
			return;
		}
	
	// Find attendance details of employee for current month
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/attendance/{id}", method=RequestMethod.GET)
	public EmployeeAttendance getEmployeeAttendance(@PathVariable int id) {
		log.debug("AttendanceController:getEmployeeAttendance - id=" + id);
		Date startDate = Date.from(LocalDate.now().withDayOfMonth(1).atStartOfDay().toInstant(ZoneOffset.UTC));
		// Date startDate = Date.from(LocalDate.now().minusMonths(1).withDayOfMonth(1).atStartOfDay().toInstant(ZoneOffset.UTC));		// For previous month
		Date endDate = new Date();
		endDate.setHours(23);
		endDate.setMinutes(59);
		endDate.setSeconds(59);
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
	
	@RequestMapping(value="/attendanceManagedBy/{id}", method=RequestMethod.GET)
	public List<EmployeeAttendance> getMonthlyAttendanceManagedBy(@PathVariable int id) {
		log.debug("AttendanceController:getAttendanceManagedBy - id=" + id);
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
	
	@SuppressWarnings("deprecation")
	private void filterDataForWeek(EmployeeAttendance ea) {
		Date today = new Date();
		int weekDay = today.getDay();
		Date startOfWeek = Date.from(LocalDate.now().minusDays(weekDay).atStartOfDay().toInstant(ZoneOffset.UTC));
		
		List<Date> ld = ea.getPresenceDate()
							.stream()
							.filter( day -> day.after(startOfWeek))
							.collect(Collectors.toList());
		ea.setPresenceDate(ld);
	}
	
	@RequestMapping(value="/weeklyAttendanceManagedBy/{id}", method=RequestMethod.GET)
	public List<EmployeeAttendance> getWeeklyAttendanceManagedBy(@PathVariable int id) {
		log.debug("AttendanceController:getAttendanceManagedBy - id=" + id);
		List<EmployeeAttendance> eaList = new ArrayList<EmployeeAttendance>();
		List<Employee> empList = empService.getEmployee();
		List<Employee> filteredList = empList
				.stream()
				.filter( emp -> emp.getManagerId() == id)
				.collect(Collectors.toList());
		for(Employee emp : filteredList) {
			EmployeeAttendance ea = getEmployeeAttendance(emp.getId());
			filterDataForWeek(ea);
			if(ea != null) {
				eaList.add(ea);
			}
		}
		
		return eaList;
	}

}
