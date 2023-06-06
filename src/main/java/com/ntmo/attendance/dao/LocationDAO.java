package com.ntmo.attendance.dao;

import java.util.List;

import com.ntmo.attendance.entity.Location;

public interface LocationDAO {
	
	List <Location> get();
	
	Location get(int id);
	
	Location save (Location loc);

	void delete(int id);
	
}
