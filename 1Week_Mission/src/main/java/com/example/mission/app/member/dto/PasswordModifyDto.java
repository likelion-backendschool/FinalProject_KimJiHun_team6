package com.example.mission.app.member.dto;

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
public class PasswordModifyDto {
	@NotEmpty
	private String oldPassword;
	@NotEmpty
	private String password;
	@NotEmpty
	private String passwordConfirm;
	public boolean confirmPassword() {
		return this.password.equals(passwordConfirm);
	}
}
