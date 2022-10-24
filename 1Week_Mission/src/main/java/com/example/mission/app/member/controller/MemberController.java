package com.example.mission.app.member.controller;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.mission.app.base.dto.RsData;
import com.example.mission.app.base.rq.Rq;
import com.example.mission.app.member.dto.ModifyDto;
import com.example.mission.app.member.dto.PasswordModifyDto;
import com.example.mission.app.member.service.MemberService;
import com.example.mission.app.member.dto.JoinDto;
import com.example.mission.app.member.entity.Member;

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
	public String memberJoinPost(@Valid @ModelAttribute JoinDto joinDto) {
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
	public String memberModify(ModifyDto modifyDto) {
		return "member/modify";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify")
	public String memberModifyPost(@Valid ModifyDto modifyDto) {
		memberService.modify(modifyDto.getEmail(), modifyDto.getNickname());
		return Rq.redirectWithMsg("/member/profile", "회원정보 수정이 완료됐습니다.");
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modifyPassword")
	public String passwordModify() {
		return "member/modify_password";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modifyPassword")
	public String passwordModifyPost(@Valid PasswordModifyDto pwModifyDto) {
		Member member = rq.getMember();
		RsData rsData = memberService.modifyPassword(member, pwModifyDto.getPassword(), pwModifyDto.getOldPassword());
		if (!pwModifyDto.confirmPassword()) {
			return rq.historyBack(rsData.getMsg());
		}
		return Rq.redirectWithMsg("/", rsData.getMsg());
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
