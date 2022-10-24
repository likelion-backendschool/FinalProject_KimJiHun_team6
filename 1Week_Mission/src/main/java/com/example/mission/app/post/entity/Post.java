package com.example.mission.app.post.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.example.mission.app.base.entity.BaseEntity;
import com.example.mission.app.member.entity.Member;

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
