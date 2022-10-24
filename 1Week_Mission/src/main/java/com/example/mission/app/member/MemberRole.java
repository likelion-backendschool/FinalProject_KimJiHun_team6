package com.example.mission.app.member;

import lombok.Getter;

@Getter
public enum MemberRole {
	AUTHOR("ROLE_AUTHOR"),
	MEMBER("ROLE_MEMBER");

	MemberRole(String value) {
		this.value = value;
	}

	private String value;
}
