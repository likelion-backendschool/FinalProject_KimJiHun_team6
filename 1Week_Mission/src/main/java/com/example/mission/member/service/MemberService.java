package com.example.mission.member.service;

import org.springframework.stereotype.Service;

import com.example.mission.member.entity.Member;
import com.example.mission.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	public Member getMemberById(long id) {
		return memberRepository.findById(id).orElse(null);
	}

	public void join(String username, String password, String email) {
		Member member = Member.builder()
			.username(username)
			.password(password)
			.email(email)
			.build();
		memberRepository.save(member);
	}
}
