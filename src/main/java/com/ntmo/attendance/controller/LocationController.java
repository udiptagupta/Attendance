package com.ntmo.attendance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntmo.attendance.Exception.LocationNotFoundException;
import com.ntmo.attendance.entity.Location;
import com.ntmo.attendance.service.LocationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
//@RequestMapping("/location")
public class LocationController {

	@Autowired
	private LocationService locationService;
	
	@RequestMapping("/locations")
	public List<Location> getLocations() {
		log.debug("LocationController:get");
		return locationService.get();
	}
	
	@PostMapping("/location")
	public Location save(@RequestBody Location loc) {
		log.info("LocationController:save " + loc);
		Location addedLoc = locationService.save(loc);
		return addedLoc;
	}
	
	@GetMapping("/location/{id}")
	public Location get(@PathVariable int id) {
		log.debug("LocationController:get " + id);
		Location loc = locationService.get(id);
		if(loc == null) {
			log.error("LocationController:get " + id + " - Not Found");
			throw new LocationNotFoundException("Location with id " + id + " not found.");
		}
		return loc;
	}
	
	@DeleteMapping("/location/{id}")
	public String delete(@PathVariable int id) {
		log.debug("LocationController:delete" + id);
		locationService.delete(id);
		return "Deleted location record with id " + id;
	}
}
