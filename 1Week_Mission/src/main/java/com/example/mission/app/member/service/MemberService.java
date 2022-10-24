package com.example.mission.app.member.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.mission.app.member.exception.AlreadyExistException;
import com.example.mission.app.member.entity.Member;
import com.example.mission.app.member.repository.MemberRepository;

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
			throw  new AlreadyExistException();
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

	public void modify(String email, String nickname) {
		if (memberRepository.existsByEmail(email)) {
			throw new AlreadyExistException();
		}
		// ToDo: 필명도 중복확인

		Member member = Member.builder()
			.email(email)
			.nickname(nickname)
			.build();
		memberRepository.save(member);
	}
}
