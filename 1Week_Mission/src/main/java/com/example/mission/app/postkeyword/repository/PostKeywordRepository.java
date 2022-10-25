package com.example.mission.app.postkeyword.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mission.app.postkeyword.entity.PostKeyword;

public interface PostKeywordRepository extends JpaRepository<PostKeyword, Long> {

	Optional<PostKeyword> findByContent(String content);
}
