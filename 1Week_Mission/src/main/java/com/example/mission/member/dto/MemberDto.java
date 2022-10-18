package com.example.mission.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
	private String username;
	private String password;
	private String passwordConfirm;
	private String email;
	private String nickname;
}
