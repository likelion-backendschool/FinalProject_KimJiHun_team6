package com.example.mission.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberController {
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/join")
	public String memberJoin() {
		return "login/join_form";
	}
}
