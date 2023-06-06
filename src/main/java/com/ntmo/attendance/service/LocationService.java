package com.ntmo.attendance.service;

import java.util.List;

import com.ntmo.attendance.entity.Location;

public interface LocationService {
	
	List <Location> get();
	
	Location get(int id);
	
	Location save (Location loc);

	void delete(int id);

}
