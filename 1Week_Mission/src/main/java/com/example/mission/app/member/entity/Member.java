package com.example.mission.app.member.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.example.mission.app.base.entity.BaseEntity;

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
public class Member extends BaseEntity {
	@Column(unique = true)
	private String username;
	private String password;
	@Column(unique = true)
	private String nickname;
	@Column(unique = true)
	private String email;
}
