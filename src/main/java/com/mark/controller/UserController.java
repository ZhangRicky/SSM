package com.mark.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mark.service.UserInter;

@Controller
@RequestMapping("/user")
public class UserController {

	
	@Autowired
	UserInter userInterImpl;
	
	@RequestMapping("/login")
	public void getUser(){
		
	}
}
