package com.ntmo.attendance.dao;

import java.util.List;
import com.ntmo.attendance.entity.Manager;

public interface ManagerDAO {
	
	List <Manager> get();
	
	Manager get(int id);
	
	Manager save (Manager loc);

	void delete(int id);

}
