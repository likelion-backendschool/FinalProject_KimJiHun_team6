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
	void memberJoin_Api_Test() throws Exception {
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
}
