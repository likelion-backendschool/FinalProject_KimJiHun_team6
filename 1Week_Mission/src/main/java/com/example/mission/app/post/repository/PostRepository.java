package com.example.mission.app.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mission.app.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
