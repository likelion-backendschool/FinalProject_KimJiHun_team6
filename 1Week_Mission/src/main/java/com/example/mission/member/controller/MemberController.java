package com.example.mission.member.controller;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.mission.base.rq.Rq;
import com.example.mission.member.dto.JoinDto;
import com.example.mission.member.service.MemberService;
import com.example.mission.security.dto.MemberContext;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	private final Rq rq;

	// 회원가입 API
	@PreAuthorize("isAnonymous()")
	@GetMapping("/join")
	public String memberJoin(JoinDto joinDto) {
		return "member/join_form";
	}

	@PostMapping("/join")
	public String memberJoinPost(@Valid @ModelAttribute JoinDto joinDto, BindingResult bindingResult) {
		if (!joinDto.confirmPassword()) {
			return "redirect:/member/join";
		}

		memberService.join(joinDto.getUsername(), joinDto.getPassword(), joinDto.getEmail());
		return Rq.redirectWithMsg("/member/login", "회원가입이 완료됐습니다.");
	}

	// 로그인 API
	@PreAuthorize("isAnonymous()")
	@GetMapping("/login")
	public String memberLogin() {
		return "member/login_form";
	}

	// 회원정보 프로필 API
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
