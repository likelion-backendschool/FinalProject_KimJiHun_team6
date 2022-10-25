package com.example.mission.app.posthashtag.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mission.app.posthashtag.entity.PostHashtag;

public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long> {
	List<PostHashtag> findAllByPostId(Long id);

	Optional<PostHashtag> findByPostIdAndPostKeywordId(Long postId, Long postKeywordId);
}
