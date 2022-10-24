package com.example.mission.app.base;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.mission.app.member.service.MemberService;

@Configuration
@Profile("test")
public class TestInitData {
	@Bean
	CommandLineRunner init(MemberService memberService, PasswordEncoder passwordEncoder) {
		return args -> {
			String password = passwordEncoder.encode("1234");
			memberService.join("user1", password, "user1@test.com");
			memberService.join("user2", password, "user2@test.com");
			memberService.join("user3", password, "user3@test.com");
			memberService.join("user4", password, "user4@test.com");
		};
	}
}
