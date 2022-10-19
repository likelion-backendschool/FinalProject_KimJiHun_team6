package com.example.mission.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mission.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
