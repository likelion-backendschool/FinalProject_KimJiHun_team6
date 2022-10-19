package com.example.mission.post.controller;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.mission.post.dto.WriteDto;
import com.example.mission.post.service.PostService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@GetMapping("/list")
	@ResponseBody
	public String postList() {
		return "글 리스트";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/write")
	public String postWrite(WriteDto writeDto) {
		return "post/write";
	}

	@PostMapping("/write")
	public String postWritePost(@Valid WriteDto writeDto) {
		postService.write(writeDto.getSubject(), writeDto.getContent());
		return "redirect:/post/list";
	}

}
