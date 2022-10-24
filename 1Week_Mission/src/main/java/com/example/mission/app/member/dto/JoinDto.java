package com.example.mission.app.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

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
public class JoinDto {
	@Size(min = 3)
	@NotEmpty(message = "사용자 이름은 필수항목입니다.")
	private String username;
	@NotEmpty(message = "비밀번호는 필수항목입니다.")
	private String password;
	@NotEmpty(message = "비밀번호 재입력은 필수항목입니다.")
	private String passwordConfirm;
	@Email
	@NotEmpty(message = "이메일은 필수항목입니다.")
	private String email;
	private String nickname;

	public boolean confirmPassword() {
		return this.password.equals(passwordConfirm);
	}
}
