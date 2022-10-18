package com.example.mission.member.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.mission.member.dto.MemberDto;
import com.example.mission.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PreAuthorize("isAnonymous()")
	@GetMapping("/join")
	public String memberJoin() {
		return "member/join_form";
	}

	@PostMapping("/join")
	public String memberJoinPost(@RequestParam MemberDto memberDto) {
		memberService.join(memberDto.getUsername(), memberDto.getPassword(), memberDto.getEmail());
		return "redirect:/member/login";
	}

	@GetMapping("/login")
	public String memberLogin() {
		return "member/login_form";
	}
}
