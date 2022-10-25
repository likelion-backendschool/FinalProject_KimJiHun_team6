package com.example.mission.app.posthashtag.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.mission.app.post.entity.Post;
import com.example.mission.app.posthashtag.entity.PostHashtag;
import com.example.mission.app.posthashtag.repository.PostHashtagRepository;
import com.example.mission.app.postkeyword.entity.PostKeyword;
import com.example.mission.app.postkeyword.service.PostKeywordService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostHashtagService {
	private final PostKeywordService postKeywordService;
	private final PostHashtagRepository postHashtagRepository;

	public void applyPostTags(Post post, String postTagContents) {
		List<PostHashtag> oldHashTags = getPostHashtags(post);

		/*
		 해시태그 문자열을 전처리하는 과정
		 #을 기준으로 문자열을 나누되 앞의 공백을 제거
		*/
		List<String> postKeywordContents = Arrays.stream(postTagContents.split("#"))
			.map(String::trim)
			.filter(s -> s.length() > 0)
			.collect(Collectors.toList());

		// 기존에 존재하는 해시태그를 제거하기 위한 로직
		List<PostHashtag> needToDelete = new ArrayList<>();

		for (PostHashtag oldHashtag : oldHashTags) {
			boolean contains = postKeywordContents.stream().anyMatch(s-> s.equals(oldHashtag.getPostKeyword().getContent()));

			if (contains == false) {
				needToDelete.add(oldHashtag);
			}
		}

		postKeywordContents.forEach(postKeywordContent -> {
			savePostTags(post,postKeywordContent);
		});
	}

	// 해시태그 중간 테이블에 값을 저장
	private PostHashtag savePostTags(Post post, String postKeywordContent) {
		PostKeyword postKeyword = postKeywordService.save(postKeywordContent);

		Optional<PostHashtag> opPostHashtag = postHashtagRepository.findByPostIdAndPostKeywordId(post.getId(), postKeyword.getId());

		if (opPostHashtag.isPresent()) {
			return opPostHashtag.get();
		}

		PostHashtag postHashtag = PostHashtag.builder()
			.post(post)
			.member(post.getAuthor())
			.postKeyword(postKeyword)
			.build();

		postHashtagRepository.save(postHashtag);

		return postHashtag;
	}

	private List<PostHashtag> getPostHashtags(Post post) {
		return postHashtagRepository.findAllByPostId(post.getId());
	}

}
