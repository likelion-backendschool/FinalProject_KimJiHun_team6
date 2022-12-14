package com.example.mission.app.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mission.app.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByUsername(String username);
	Optional<Member> findByEmail(String email);
	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	Optional<Member> findByUsernameAndEmail(String username, String email);
}
