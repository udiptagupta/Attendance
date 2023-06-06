package com.ntmo.attendance.service;

import java.util.List;

import com.ntmo.attendance.entity.Employee;

public interface EmployeeService {
	
	public List<Employee> getEmployee();
	
	public Employee get(int id);
	
	public Employee save(Employee emp);
	
	public void delete(int id);
	
	public Employee findEmployee(String employeeEmail);

}
