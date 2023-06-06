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


import com.ntmo.attendance.entity.Manager;
import com.ntmo.attendance.service.ManagerService;

@RestController
public class ManagerController {
	
	@Autowired
	private ManagerService managerService;
	
	@RequestMapping("/managers")
	public List<Manager> getManagers() {
		return managerService.get();
	}

	
	@PostMapping("/manager")
	public Manager save(@RequestBody Manager mgr) {
		Manager newMgr = managerService.save(mgr);
		return newMgr;
	}
	
	@GetMapping("/manager/{id}")
	public Manager get(@PathVariable int id) {
		Manager mgr = managerService.get(id);
		if(mgr == null) {
			throw new RuntimeException("Manager with id " + id + " not found.");
		}
		return mgr;
	}
	
	@DeleteMapping("/manager/{id}")
	public String delete(@PathVariable int id) {
		managerService.delete(id);
		return "Deleted Manager record with id " + id;
	}

}
