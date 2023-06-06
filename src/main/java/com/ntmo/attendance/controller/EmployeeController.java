package com.ntmo.attendance.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntmo.attendance.entity.Employee;
import com.ntmo.attendance.entity.Manager;
import com.ntmo.attendance.service.EmployeeService;
import com.ntmo.attendance.service.ManagerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class EmployeeController {
	
	@Autowired
	private EmployeeService empService;
	
	@Autowired
	private ManagerService mgrService;

	@RequestMapping("/employees")
	public List<Employee> getEmployees() {
		log.debug("EmployeeController:getEmployees");
		return empService.getEmployee();
	}

	
	@PostMapping("/employee")
	public Employee save(@RequestBody Employee emp) {
		log.info("EmployeeController:save - " + emp);
		return empService.save(emp);
	}
	
	@GetMapping("/employee/{id}")
	public Employee get(@PathVariable int id) {
		log.debug("EmployeeController:get - " + id);
		return empService.get(id);
	}

	@GetMapping("/findEmployee/{email}")
	public Employee findEmployee(@PathVariable String email) {
		log.debug("EmployeeController:findEmployee - " + email);
		return empService.findEmployee(email);
	}
	
	@DeleteMapping("/employee/{id}")
	public String delete(@PathVariable int id) {
		log.debug("EmployeeController:delete - " + id);
		empService.delete(id);
		return "Removed Employee record with id " + id + ".";
	}
	
	@GetMapping("/employeesManagedBy/{id}")
	public List<Employee> getManagedEmployees(@PathVariable int id) {
		log.debug("EmployeeController:getManagedEmployees - " + id);
		List<Employee> empList = empService.getEmployee();
		List<Employee> filteredList = empList
		.stream()
		.filter( emp -> emp.getManagerId() == id)
		.collect(Collectors.toList());
		return filteredList;
	}
	
	@GetMapping("/isManager/{email}")
	public Manager isManager(@PathVariable String email) {
		log.debug("EmployeeController:isManager - " + email);
		if(null == findEmployee(email)) {
			return null;
		}
		
		List <Manager> managers = mgrService.get();
		for(Manager mgr: managers) {
			if(email.equalsIgnoreCase(mgr.getName())) {
				return mgr;
			}
		}
		return null;
	}
}
