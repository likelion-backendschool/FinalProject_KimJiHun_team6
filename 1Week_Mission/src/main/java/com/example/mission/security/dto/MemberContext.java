package com.example.mission.security.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.example.mission.member.entity.Member;

import lombok.Getter;

@Getter
public class MemberContext extends User {
	private final Long id;
	private final LocalDateTime createDate;
	private final LocalDateTime modifyDate;
	private final String username;
	private final String email;
	private final String nickname;
	public MemberContext(Member member, List<GrantedAuthority> authorities) {
		super(member.getUsername(), member.getPassword(), authorities);
		this.id = member.getId();
		this.createDate = member.getCreateDate();
		this.modifyDate = member.getModifyDate();
		this.username = member.getUsername();
		this.email = member.getEmail();
		this.nickname = member.getNickname();
	}

	public Member getMember() {
		return Member
			.builder()
			.id(id)
			.createDate(createDate)
			.modifyDate(modifyDate)
			.username(username)
			.email(email)
			.nickname(nickname)
			.build();
	}
}
