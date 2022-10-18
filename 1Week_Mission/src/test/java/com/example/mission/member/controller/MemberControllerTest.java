package com.example.mission.member.controller;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;

import com.example.mission.member.dto.MemberDto;
import com.example.mission.member.entity.Member;
import com.example.mission.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class MemberControllerTest {

	@Autowired
	private MockMvc mvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private MemberService memberService;

	@Test
	@DisplayName("Get /member/join 은 회원가입 폼 가져오는 URL 이다.")
	void memberJoin_GetApi_Test() throws Exception {
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
	void memberJoin_PostApi_Test() throws Exception {
		// Given
		MemberDto memberDto = MemberDto.builder()
			.username("user5")
			.password("1234")
			.email("user5@test.com")
			.build();

		String content = objectMapper.writeValueAsString(memberDto);

		// When
		ResultActions resultActions = mvc.perform(
				post("/member/join")
					.content(content)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
			)
			.andDo(print());

		resultActions
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/member/login"))
			.andExpect(handler().handlerType(MemberController.class))
			.andExpect(handler().methodName("memberJoinPost"));

		Member member = memberService.getMemberById(5L);

		// Then
		assertThat(member).isNotNull();
	}

	@Test
	@DisplayName("Get /member/login 은 로그인폼을 가져오는 URL 이다.")
	void memberlogin_GetApi_Test() throws Exception {
		ResultActions resultActions = mvc
			.perform(get("/member/login"))
			.andDo(print());

		resultActions
			.andExpect(status().is2xxSuccessful())
			.andExpect(handler().handlerType(MemberController.class))
			.andExpect(handler().methodName("memberLogin"));
	}

	@Test
	@DisplayName("user4로 로그인 후 프로필페이지에 접속하면 user4의 이메일이 보여야 한다.")
	@WithUserDetails("user4")
	void memberlogin_PostApi_Test() throws Exception {
		ResultActions resultActions = mvc
			.perform(
				get("/member/profile")
			)
			.andDo(print());

		resultActions
			.andExpect(status().is2xxSuccessful())
			.andExpect(handler().handlerType(MemberController.class))
			.andExpect(handler().methodName("memberProfile"))
			.andExpect(content().string(containsString("user4@test.com")));
	}
}
