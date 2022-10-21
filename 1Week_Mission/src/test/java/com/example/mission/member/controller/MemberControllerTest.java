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

import com.example.mission.member.dto.JoinDto;
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
	@DisplayName("회원가입폼")
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
	@DisplayName("회원가입 처리")
	void memberJoin_PostApi_Test() throws Exception {
		// Given
		JoinDto memberDto = JoinDto.builder()
			.username("user5")
			.password("1234")
			.passwordConfirm("1234")
			.email("user5@test.com")
			.build();

		String content = objectMapper.writeValueAsString(memberDto);
		// When
		ResultActions resultActions = mvc.perform(
				post("/member/join")
					.content(content)
					.contentType(MediaType.APPLICATION_JSON)
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
	@DisplayName("로그인폼")
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
	@DisplayName("유저 이메일을 프로필 페이지에 표시")
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
