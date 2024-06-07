package com.project.npp.controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
	
	@GetMapping("/all")
	public String publicAccess() {
		return "Public Content.";
	}
	@GetMapping("/systemadmin")
	public String userAccess() {
		return "System Admin.";
	}

	@GetMapping("/compliance")
	public String parentAccess() {
		return "Compliance Officer.";
	}
	
	@GetMapping("/customerservice")
	public String childAccess() {
		return "Customer Service.";
	}
	
	
}
