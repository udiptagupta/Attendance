package com.ntmo.attendance.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ntmo.attendance.entity.Employee;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class EmployeeDAOImpl implements EmployeeDAO {
	
	@Autowired
	private EntityManager entityManager;

	@Override
	public List<Employee> getEmployee() {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Employee> query = currentSession.createQuery("from Employee", Employee.class);
		List<Employee> list = query.getResultList();
		return list;
	}

	@Override
	public Employee get(int id) {
		Session currentSession = entityManager.unwrap(Session.class);
		Employee emp = currentSession.get(Employee.class, id);
		return emp;
	}

	@Override
	public Employee save(Employee emp) {
		Session currentSession = entityManager.unwrap(Session.class);
		currentSession.persist(emp);
		return emp;
	}

	@Override
	public void delete(int id) {
		Session currentSession = entityManager.unwrap(Session.class);
		Employee emp = currentSession.get(Employee.class, id);
		if(emp == null) {
			log.error("EmployeeDAOImpl:delete " + id + " Not Found");
			throw new RuntimeException("Employee with id " + id + " not found.");
		}
		currentSession.remove(emp);
	}
	
	@Override
	public Employee findEmployee(String employeeMail) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Employee> query = currentSession.createQuery("from Employee where employeeMail = \'" + employeeMail + "\'", Employee.class);
		Employee emp = query.getSingleResultOrNull();
		return emp;
	}

}
