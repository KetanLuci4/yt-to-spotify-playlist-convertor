package com.resh.coders.springmvc.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class HomeContoller {
	
	@GetMapping(value = "/getYoutubeApiURIs")
	public String home() {
		return "forward:/index.htmldscfdfdfdsfdssfdsfsfsdsf";
		
	}
	
	// @RequestMapping(value = "/welcome")
	// public String home() {
	// 	return "WELCOME TO HOME";
		
	// }


}
