package com.ntmo.attendance.service;

import java.util.List;

import com.ntmo.attendance.entity.Manager;

public interface ManagerService {
	
	List <Manager> get();
	
	Manager get(int id);
	
	Manager save (Manager mgr);

	void delete(int id);

}
