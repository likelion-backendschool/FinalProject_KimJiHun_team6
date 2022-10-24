package com.example.mission.post.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.example.mission.app.post.controller.PostController;
import com.example.mission.app.post.service.PostService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class PostControllerTest {
	@Autowired
	private MockMvc mvc;
	@Autowired
	private PostService postService;

	@Test
	@DisplayName("글 작성 폼")
	@WithUserDetails("user1")
	void postWrite_GetApi_Test() throws Exception {
		// WHEN
		ResultActions resultActions = mvc
			.perform(get("/post/write"))
			.andDo(print());

		// THEN
		resultActions
			.andExpect(status().is2xxSuccessful())
			.andExpect(handler().handlerType(PostController.class))
			.andExpect(handler().methodName("postWrite"))
			.andExpect(content().string(containsString("글 작성")));
	}

}
