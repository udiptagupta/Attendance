package com.ntmo.attendance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ntmo.attendance.dao.EmployeeDAO;
import com.ntmo.attendance.entity.Employee;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeDAO empDAO;
	
	@Override
	@Transactional
	public List<Employee> getEmployee() {
		return empDAO.getEmployee();
	}

	@Override
	@Transactional
	public Employee get(int id) {
		return empDAO.get(id);
	}

	@Override
	@Transactional
	public Employee save(Employee emp) {
		return empDAO.save(emp);
	}

	@Override
	@Transactional
	public void delete(int id) {
		empDAO.delete(id);
	}
	@Override
	@Transactional
	public Employee findEmployee(String employeeEmail) {
		return empDAO.findEmployee(employeeEmail);
	}

}
