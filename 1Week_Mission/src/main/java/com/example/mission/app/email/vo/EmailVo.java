package com.example.mission.app.email.vo;

import java.util.Objects;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EmailVo {
	private final String address;
	private final String subject;
	private final String body;

	public EmailVo(String address, String subject, String body) {
		this.address = address;
		this.subject = subject;
		this.body = body;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EmailVo emailVo = (EmailVo)o;
		return Objects.equals(address, emailVo.address) && Objects.equals(subject, emailVo.subject)
			&& Objects.equals(body, emailVo.body);
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, subject, body);
	}
}
