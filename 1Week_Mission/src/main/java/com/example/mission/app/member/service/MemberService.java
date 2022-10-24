package com.example.mission.app.member.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.mission.app.base.dto.RsData;
import com.example.mission.app.email.vo.EmailVo;
import com.example.mission.app.member.entity.Member;
import com.example.mission.app.member.exception.AlreadyExistException;
import com.example.mission.app.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final JavaMailSender mailSender;

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

		sendMail(email, "회원가입 축하메일" , "멋사북에 가입한걸 환영합니다!🥳");
	}

	public Optional<Member> findByEmail(String email) {
		return memberRepository.findByEmail(email);
	}

	public Optional<Member> findByUsernameAndEmail(String username, String email) {
		return memberRepository.findByUsernameAndEmail(username, email);
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

	@Transactional
	public RsData modifyPassword(Member member, String password, String oldPassword) {
		Member oldMember = memberRepository.findById(member.getId()).get();
		if (!passwordEncoder.matches(oldPassword, oldMember.getPassword())) {
			return RsData.of("F-1", "기존 비밀번호가 일치하지 않습니다.");
		}
		oldMember = Member.builder()
			.password(password)
			.build();

		return RsData.of("S-1", "비밀번호가 변경되었습니다.");
	}

	public void sendMail(String email, String subject, String body) {
		EmailVo vo = EmailVo.builder()
			.address("gi020477@gmail.com")
			.subject(subject)
			.body(body)
			.build();

		SimpleMailMessage smm = new SimpleMailMessage();
		smm.setFrom(vo.getAddress());
		smm.setTo(email);
		smm.setSubject(vo.getSubject());
		smm.setText(vo.getBody());
		mailSender.send(smm);
	}

	public void saveTempPassword(String username, int tempNum) {
		Member member = memberRepository.findByUsername(username).get();
		member.setPassword(passwordEncoder.encode(Integer.toString(tempNum)));
		memberRepository.save(member);
	}
}
