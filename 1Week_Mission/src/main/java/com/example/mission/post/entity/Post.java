package com.example.mission.post.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.example.mission.base.entity.BaseEntity;
import com.example.mission.member.entity.Member;
import com.example.mission.post.dto.PostModifyDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Post extends BaseEntity {
	private String subject;
	private String content;
	private String contentHtml;

	@ManyToOne
	private Member member;
}
