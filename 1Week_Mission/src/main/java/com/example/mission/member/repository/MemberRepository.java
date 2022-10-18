package com.example.mission.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mission.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
