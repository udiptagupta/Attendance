package com.ntmo.attendance.dao;

import java.util.List;

import com.ntmo.attendance.entity.Holidays;

public interface HolidaysDAO {
	
	List<Holidays> get();
	Holidays save(Holidays day);
	void delete (int id);
	
}
