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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ManagerController {
	
	@Autowired
	private ManagerService managerService;
	
	@RequestMapping("/managers")
	public List<Manager> getManagers() {
		log.debug("ManagerController:getManagers");
		return managerService.get();
	}

	
	@PostMapping("/manager")
	public Manager save(@RequestBody Manager mgr) {
		log.info("ManagerController:save" + mgr);
		Manager newMgr = managerService.save(mgr);
		return newMgr;
	}
	
	@GetMapping("/manager/{id}")
	public Manager get(@PathVariable int id) {
		log.debug("ManagerController:get " + id);
		Manager mgr = managerService.get(id);
		if(mgr == null) {
			log.error("ManagerController:get " + id + " - Not Found");
			throw new RuntimeException("Manager with id " + id + " not found.");
		}
		return mgr;
	}
	
	@DeleteMapping("/manager/{id}")
	public String delete(@PathVariable int id) {
		log.debug("ManagerController:delete " + id);
		managerService.delete(id);
		return "Deleted Manager record with id " + id;
	}

}
