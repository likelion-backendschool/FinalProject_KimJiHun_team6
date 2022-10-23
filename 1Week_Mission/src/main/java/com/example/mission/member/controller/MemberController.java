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
import com.example.mission.member.entity.Member;
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
			return Rq.redirectWithMsg("/member/join", "회원가입을 실패했습니다.");
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
	public String memberModify() {
		return "member/modify";
	}

	@PostMapping("/modify")
	public String memberModifyPost() {

		return "member/profile";
	}

	// 아이디, 비밀번호 찾기 폼
	@PreAuthorize("isAnonymous()")
	@GetMapping("/findUsername")
	public String memberFindUsername() {
		return "member/find_username";
	}

	@PreAuthorize("isAnonymous()")
	@PostMapping("/findUsername")
	public String memberFindUsernamePost(String email) {
		Member member = memberService.findByEmail(email).orElse(null);
		if (member == null) {
			return rq.historyBack("해당 이메일은 존재하지 않습니다.");
		}
		return Rq.redirectWithMsg("/member/login?username=%s".formatted(member.getUsername()), "해당 이메일로 가입한 계정의 아이디는 '%s' 입니다.".formatted(member.getUsername()));
	}
}
