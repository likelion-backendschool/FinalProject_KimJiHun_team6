package com.example.mission.post.entity;

import javax.persistence.Entity;

import com.example.mission.base.entity.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Post extends BaseEntity {
	private String subject;
	private String content;
	private String contentHtml;
}
