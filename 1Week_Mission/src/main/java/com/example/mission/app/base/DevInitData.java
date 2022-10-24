package com.example.mission.app.base;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.example.mission.app.member.service.MemberService;
import com.example.mission.app.post.service.PostService;

@Configuration
@Profile("dev")
public class DevInitData {
	@Bean
	CommandLineRunner init(MemberService memberService, PostService postService) {
		return args -> {
			String password = "1234";
			memberService.join("user1", password, "user1@test.com");
			memberService.join("user2", password, "user2@test.com");

			for (int i = 1; i <= 100; i++) {
				if (i % 2 == 0) {
					postService.write(1L, "제목%d".formatted(i), "# 내용 %d".formatted(i), "<h1>내용 %d</h1>".formatted(i));
				}
				if (i % 2 == 1) {
					postService.write(2L, "제목%d".formatted(i), "# 내용 %d".formatted(i), "<h1>내용 %d</h1>".formatted(i));
				}
			}
		};
	}
}
