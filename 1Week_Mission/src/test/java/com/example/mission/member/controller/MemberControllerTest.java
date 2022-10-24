package com.example.mission.member.controller;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.mission.app.member.controller.MemberController;
import com.example.mission.app.member.entity.Member;
import com.example.mission.app.member.service.MemberService;
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
	@DisplayName("회원가입 성공")
	void memberJoin_PostApi_Test() throws Exception {
		// When
		ResultActions resultActions = mvc.perform(
				post("/member/join")
					.param("username", "user5")
					.param("password", "1234")
					.param("passwordConfirm","1234")
					.param("email", "user5@test.com")
			)
			.andDo(print());

		resultActions
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("/member/login?msg=**"))
			.andExpect(handler().handlerType(MemberController.class))
			.andExpect(handler().methodName("memberJoinPost"));

		Member member = memberService.getMemberById(5L);

		// Then
		assertThat(member).isNotNull();
	}

	@Test
	@DisplayName("비밀번호 재입력이 틀렸을시 회원가입 실패")
	void memberJoin_PostApi_PasswordIncorrect_Test() throws Exception {
		// When
		ResultActions resultActions = mvc.perform(
				post("/member/join")
					.param("username", "user5")
					.param("password", "1234")
					.param("passwordConfirm", "12345")
					.param("email", "user5@test.com")
			)
			.andDo(print());

		resultActions
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("/member/join?msg=**"))
			.andExpect(handler().handlerType(MemberController.class))
			.andExpect(handler().methodName("memberJoinPost"));

		assertThat(memberService.findByUsername("user5")).isNull();
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

	@Test
	@DisplayName("아이디찾기 폼")
	void memberFindUsername_GetApi_Test() throws Exception {
		ResultActions resultActions = mvc
			.perform(get("/member/findUsername"))
			.andDo(print());

		resultActions
			.andExpect(status().is2xxSuccessful())
			.andExpect(handler().handlerType(MemberController.class))
			.andExpect(handler().methodName("memberFindUsername"));
	}

	@Test
	@DisplayName("아이디찾기 성공")
	void memberFindUsername_PostApi_Test() throws Exception {
		ResultActions resultActions = mvc
			.perform(
				post("/member/findUsername")
				.param("email", "user1@test.com")
			)
			.andDo(print());

		resultActions
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("/member/login?**"))
			.andExpect(handler().handlerType(MemberController.class))
			.andExpect(handler().methodName("memberFindUsernamePost"));
	}

	@Test
	@DisplayName("회원정보 수정폼")
	@WithUserDetails("user2")
	void memberModify_GetApi_Test() throws Exception {
		ResultActions resultActions = mvc
			.perform(get("/member/modify"))
			.andDo(print());

		resultActions
			.andExpect(status().is2xxSuccessful())
			.andExpect(handler().handlerType(MemberController.class))
			.andExpect(handler().methodName("memberModify"));
	}

	@Test
	@DisplayName("회원정보 수정 성공")
	@WithUserDetails("user2")
	void memberModify_PostApi_Test() throws Exception {
		ResultActions resultActions = mvc
			.perform(
				post("/member/modify")
					.param("email", "user22@test.com")
					.param("nickname", "kjh")
			)
			.andDo(print());

		resultActions
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("/member/profile?msg=**"))
			.andExpect(handler().handlerType(MemberController.class))
			.andExpect(handler().methodName("memberModifyPost"));

		assertThat(memberService.findByEmail("user22@test.com").isPresent()).isTrue();
	}

	@Test
	@DisplayName("비밀번호 수정폼")
	@WithUserDetails("user3")
	void passwordModify_GetApi_Test() throws Exception {
		ResultActions resultActions = mvc
			.perform(get("/member/modifyPassword"))
			.andDo(print());

		resultActions
			.andExpect(status().is2xxSuccessful())
			.andExpect(handler().handlerType(MemberController.class))
			.andExpect(handler().methodName("passwordModify"));
	}

	@Test
	@DisplayName("비밀번호 수정 성공")
	@WithUserDetails("user3")
	void passwordModify_PostApi_Test() throws Exception {
		ResultActions resultActions = mvc
			.perform(
				post("/member/modifyPassword")
					.param("oldPassword", "1234")
					.param("password", "12345")
					.param("passwordConfirm", "12345")
			)
			.andDo(print());

		resultActions
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("/?msg=**"))
			.andExpect(handler().handlerType(MemberController.class))
			.andExpect(handler().methodName("passwordModifyPost"));
	}

	@Test
	@DisplayName("비밀번호찾기 폼")
	void memberFindPassword_GetApi_Test() throws Exception {
		ResultActions resultActions = mvc
			.perform(get("/member/findPassword"))
			.andDo(print());

		resultActions
			.andExpect(status().is2xxSuccessful())
			.andExpect(handler().handlerType(MemberController.class))
			.andExpect(handler().methodName("memberFindPassword"));
	}
}
