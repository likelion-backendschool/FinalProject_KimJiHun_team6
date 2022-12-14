package com.example.mission.app.post.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.mission.app.member.entity.Member;
import com.example.mission.app.member.repository.MemberRepository;
import com.example.mission.app.post.dto.PostModifyDto;
import com.example.mission.app.post.entity.Post;
import com.example.mission.app.post.repository.PostRepository;
import com.example.mission.app.posthashtag.entity.PostHashtag;
import com.example.mission.app.posthashtag.service.PostHashtagService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	private final PostHashtagService postHashtagService;
	@Transactional
	public Post write(Member author, String subject, String content, String contentHtml, String postTagContents) {
		Post post = Post.builder()
			.subject(subject)
			.content(content)
			.contentHtml(contentHtml)
			.author(author)
			.build();
		postRepository.save(post);

		postHashtagService.applyPostTags(post, postTagContents);
		return post;
	}

	public List<Post> findAll() {
		return postRepository.findAll();
	}

	public Post findById(Long id) {
		return postRepository.findById(id).orElse(null);
	}

	public void modify(Long id, String subject, String content, String contentHtml) {
		Post post = postRepository.findById(id).orElse(null);
		post.setSubject(subject);
		post.setContent(content);
		post.setContentHtml(contentHtml);
		postRepository.save(post);
	}

	public void delete(Post post) {
		postRepository.delete(post);
	}

	public List<Post> findAllForPrintByAuthorIdOrderByIdDesc(long authorId) {
		List<Post> posts = postRepository.findAllByAuthorIdOrderByIdDesc(authorId);
		loadForPrintData(posts);

		return posts;
	}

	private void loadForPrintData(List<Post> posts) {
		long[] ids = posts
			.stream()
			.mapToLong(Post::getId)
			.toArray();
	}

	// Entity??? Dto??? ??????????????? ?????????
	public PostModifyDto convert(Post post) {
		PostModifyDto postModifyDto = PostModifyDto.builder()
			.id(post.getId())
			.subject(post.getSubject())
			.content(post.getContent())
			.contentHtml(post.getContentHtml())
			.build();
		return postModifyDto;
	}
}
