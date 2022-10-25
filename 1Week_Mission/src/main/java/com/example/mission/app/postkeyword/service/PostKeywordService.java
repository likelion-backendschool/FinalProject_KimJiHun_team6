package com.example.mission.app.postkeyword.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.mission.app.postkeyword.entity.PostKeyword;
import com.example.mission.app.postkeyword.repository.PostKeywordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostKeywordService {

	private final PostKeywordRepository postKeywordRepository;
	public PostKeyword save(String content) {
		Optional<PostKeyword> optKeyword = postKeywordRepository.findByContent(content);

		if (optKeyword.isPresent()) {
			return optKeyword.get();
		}

		PostKeyword postKeyword = PostKeyword
			.builder()
			.content(content)
			.build();

		postKeywordRepository.save(postKeyword);

		return postKeyword;
	}
}
