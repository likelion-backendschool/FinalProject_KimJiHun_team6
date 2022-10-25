package com.example.mission.app.home.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.mission.app.base.rq.Rq;
import com.example.mission.app.post.entity.Post;
import com.example.mission.app.post.service.PostService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
	private final PostService postService;
	private final Rq rq;

	@GetMapping("/")
	public String home(Model model) {
		if ( rq.isLogined() ) {
			List<Post> posts = postService.findAllForPrintByAuthorIdOrderByIdDesc(rq.getId());
			model.addAttribute("posts", posts);
		}
		return "home";
	}
}
