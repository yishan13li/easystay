package com.easystay.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class IndexController {

	@GetMapping("/home")
	public String home() {
		return "系統首頁";
	}
	
}
