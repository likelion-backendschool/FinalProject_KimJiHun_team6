package com.example.mission.post.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mission.member.entity.Member;
import com.example.mission.member.repository.MemberRepository;
import com.example.mission.post.entity.Post;
import com.example.mission.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	public void write(Long id, String subject, String content) {
		Member member = memberRepository.findById(id).orElseThrow();
		Post post = Post.builder()
			.member(member)
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

	public void delete(Post post) {
		postRepository.delete(post);
	}
}
