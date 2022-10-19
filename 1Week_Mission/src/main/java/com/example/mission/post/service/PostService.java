package com.example.mission.post.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mission.post.entity.Post;
import com.example.mission.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	public void write(String subject, String content) {
		Post post = Post.builder()
			.subject(subject)
			.content(content)
			.build();
		postRepository.save(post);
	}

	public List<Post> findAll() {
		return postRepository.findAll();
	}

	public Post findById(Long id) {
		return postRepository.findById(id).orElse(null);
	}

	public void modify(Long id, String subject, String content) {
		Post post = postRepository.findById(id).orElse(null);
		post.setSubject(subject);
		post.setContent(content);
		postRepository.save(post);
	}
}
