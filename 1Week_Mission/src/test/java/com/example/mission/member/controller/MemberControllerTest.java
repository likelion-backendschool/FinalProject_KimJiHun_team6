package com.example.mission.member.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;

import com.example.mission.member.entity.Member;
import com.example.mission.member.service.MemberService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class MemberControllerTest {

	@Autowired
	private MockMvc mvc;
	@Autowired
	private MemberService memberService;

	@Test
	@DisplayName("Get /member/join 은 회원가입 폼 가져오는 URL 이다.")
	void t1() throws Exception {
		ResultActions resultActions = mvc
			.perform(get("/member/join"))
			.andDo(print());

		resultActions
			.andExpect(status().is2xxSuccessful())
			.andExpect(handler().handlerType(MemberController.class))
			.andExpect(handler().methodName("memberJoin"));

	}
	@Test
	@DisplayName("POST /member/join 은 회원가입 처리 URL 이다.")
	void t2() throws Exception {
		ResultActions resultActions = mvc.perform(
				multipart("/member/join")
					.param("username", "user5")
					.param("password", "1234")
					.param("email", "user5@test.com")
					.characterEncoding("UTF-8"))
			.andDo(print());

		resultActions
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/member/login"))
			.andExpect(handler().handlerType(MemberController.class))
			.andExpect(handler().methodName("join"));

		Member member = memberService.getMemberById(5L);

		assertThat(member).isNotNull();
	}
}
