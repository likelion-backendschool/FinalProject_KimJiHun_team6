package com.example.mission.app.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.mission.app.member.MemberRole;
import com.example.mission.app.member.entity.Member;
import com.example.mission.app.member.repository.MemberRepository;
import com.example.mission.app.security.dto.MemberContext;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberSecurityService implements UserDetailsService {
	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Member> _member = memberRepository.findByUsername(username);
		if (_member.isEmpty()) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}
		Member member = _member.get();

		List<GrantedAuthority> authorities = new ArrayList<>();
		if (member.getNickname() != null) {
			authorities.add(new SimpleGrantedAuthority(MemberRole.AUTHOR.getValue()));
		} else {
			authorities.add(new SimpleGrantedAuthority(MemberRole.MEMBER.getValue()));
		}
		return new MemberContext(member, authorities);
	}
}