package com.example.mission.member.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.mission.member.entity.Member;
import com.example.mission.member.exception.AlreadyJoinException;
import com.example.mission.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	public Member getMemberById(long id) {
		return memberRepository.findById(id).orElse(null);
	}

	public Member findByUsername(String username) {
		return memberRepository.findByUsername(username).orElse(null);
	}

	@Transactional
	public void join(String username, String password, String email) {
		if (memberRepository.existsByUsername(username)) {
			throw  new AlreadyJoinException();
		}

		Member member = Member.builder()
			.username(username)
			.password(passwordEncoder.encode(password))
			.email(email)
			.build();
		memberRepository.save(member);

		// ToDo: 회원가입 축하 메일 보내기
	}

	public Optional<Member> findByEmail(String email) {
		return memberRepository.findByEmail(email);
	}
}
