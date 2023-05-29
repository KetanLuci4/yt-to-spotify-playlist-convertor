package com.resh.coders.springmvc.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeContoller {
	
	//@RequestMapping(value = "/")
	public String home() {
		return "WELCOME TO HOME";		
	}


}
