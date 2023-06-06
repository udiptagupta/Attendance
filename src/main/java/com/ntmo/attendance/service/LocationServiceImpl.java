package com.ntmo.attendance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ntmo.attendance.dao.LocationDAO;
import com.ntmo.attendance.entity.Location;

@Service
public class LocationServiceImpl implements LocationService{

	@Autowired
	private LocationDAO locationDAO;
	
	@Transactional
	@Override
	public List<Location> get() {
		return locationDAO.get();
	}

	@Transactional
	@Override
	public Location get(int id) {
		return locationDAO.get(id);
	}

	@Transactional
	@Override
	public Location save(Location loc) {
		locationDAO.save(loc);
		
		return loc;
	}

	@Transactional
	@Override
	public void delete(int id) {
		locationDAO.delete(id);
	}

}
