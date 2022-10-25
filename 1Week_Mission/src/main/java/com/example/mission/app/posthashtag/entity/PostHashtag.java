package com.example.mission.app.posthashtag.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.mission.app.base.entity.BaseEntity;
import com.example.mission.app.member.entity.Member;
import com.example.mission.app.post.entity.Post;
import com.example.mission.app.postkeyword.entity.PostKeyword;

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
public class PostHashtag extends BaseEntity {
	@ManyToOne
	@ToString.Exclude
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Post post;
	@ManyToOne
	@ToString.Exclude
	private Member member;
	@ManyToOne
	@ToString.Exclude
	private PostKeyword postKeyword;
}
