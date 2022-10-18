package com.example.mission.security.dto;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.example.mission.member.entity.Member;

import lombok.Getter;

@Getter
public class MemberContext extends User {
	private final Long id;
	private final String email;

	public MemberContext(Member member, List<GrantedAuthority> authorities) {
		super(member.getUsername(), member.getPassword(), authorities);
		this.id = member.getId();
		this.email = member.getEmail();
	}
}
