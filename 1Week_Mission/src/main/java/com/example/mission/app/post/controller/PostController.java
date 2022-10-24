package com.example.mission.app.post.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.example.mission.app.post.dto.PostModifyDto;
import com.example.mission.app.post.entity.Post;
import com.example.mission.app.security.dto.MemberContext;
import com.example.mission.app.post.dto.WriteDto;
import com.example.mission.app.post.service.PostService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@GetMapping("/list")
	public String postList(Model model) {
		List<Post> posts = postService.findAll();
		model.addAttribute("posts",posts);
		return "post/list";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/write")
	public String postWrite(WriteDto writeDto) {
		return "post/write";
	}

	@PostMapping("/write")
	public String postWritePost(@AuthenticationPrincipal MemberContext memberContext, @Valid WriteDto writeDto) {
		postService.write(memberContext.getId(), writeDto.getSubject(), writeDto.getContent());
		return "redirect:/post/list";
	}

	@GetMapping("/{id}")
	public String postDetail(Model model, @PathVariable("id") Long id) {
		Post post = postService.findById(id);
		model.addAttribute("post", post);
		return "post/detail";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/{id}/modify")
	public String postModify(Model model, @PathVariable("id") Long id) {
		Post post = postService.findById(id);
		PostModifyDto postModifyDto = postService.convert(post);
		model.addAttribute("postModifyDto", postModifyDto);
		return "post/modify";
	}

	@PostMapping("/{id}/modify")
	public String postModifyPost(@Valid PostModifyDto postModifyDto, @PathVariable("id") Long id) {
		postService.modify(id, postModifyDto.getSubject(), postModifyDto.getContent());
		return "redirect:/post/list";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/{id}/delete")
	public String postDelete(@AuthenticationPrincipal MemberContext memberContext, @PathVariable("id") Long id) {
		Post post = this.postService.findById(id);
		if (!post.getMember().getUsername().equals(memberContext.getUsername())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
		}
		this.postService.delete(post);
		return "redirect:/post/list";
	}
}
