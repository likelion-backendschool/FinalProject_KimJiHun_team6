package com.example.mission.post.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/post")
public class PostController {

	@GetMapping("/list")
	@ResponseBody
	public String postList() {
		return "글 리스트";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/write")
	public String postWrite() {
		return "post/write";
	}

}
