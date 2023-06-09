package com.ntmo.attendance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SwaggerController {
	
	
	/**
	 * Home redirection to OpenAPI api documentation
	 */
    @RequestMapping("/")
    public String index() {
        return "redirect:swagger-ui.html";
    }
	
}
