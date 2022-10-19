package com.example.mission.member.controller;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.mission.member.dto.JoinDto;
import com.example.mission.member.service.MemberService;
import com.example.mission.security.dto.MemberContext;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;

	@PreAuthorize("isAnonymous()")
	@GetMapping("/join")
	public String memberJoin(JoinDto joinDto) {
		return "member/join_form";
	}

	@PostMapping("/join")
	public String memberJoinPost(@Valid JoinDto joinDto) {
		if (!joinDto.confirmPassword()) {
			return "member/join_form";
		}
		memberService.join(joinDto.getUsername(),
			passwordEncoder.encode(joinDto.getPassword()), joinDto.getEmail());
		return "redirect:/member/login";
	}

	@PreAuthorize("isAnonymous()")
	@GetMapping("/login")
	public String memberLogin() {
		return "member/login_form";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/profile")
	public String memberProfile() {
		return "member/profile";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify")
	public String memberModifyForm() {
		return "member/modify";
	}

	@PostMapping("/modify")
	public String memberModify(@AuthenticationPrincipal MemberContext memberContext) {
		return "member/profile";
	}

}
