package com.example.mission.app.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifyDto {
	@Email
	@NotEmpty(message = "이메일은 필수항목입니다.")
	private String email;
	private String nickname;
}
