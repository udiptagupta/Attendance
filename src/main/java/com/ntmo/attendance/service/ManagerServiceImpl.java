package com.ntmo.attendance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ntmo.attendance.dao.ManagerDAO;
import com.ntmo.attendance.entity.Manager;

@Service
public class ManagerServiceImpl implements ManagerService {

	@Autowired
	private ManagerDAO managerDAO;
	
	@Override
	@Transactional
	public List<Manager> get() {
		return managerDAO.get();
	}

	@Override
	@Transactional
	public Manager get(int id) {
		return managerDAO.get(id);
	}

	@Override
	@Transactional
	public Manager save(Manager mgr) {
		return managerDAO.save(mgr);
	}

	@Override
	@Transactional
	public void delete(int id) {
		managerDAO.delete(id);
	}

}
