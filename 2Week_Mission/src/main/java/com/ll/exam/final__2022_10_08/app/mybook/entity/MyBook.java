package com.ll.exam.final__2022_10_08.app.mybook.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.ll.exam.final__2022_10_08.app.base.entity.BaseEntity;
import com.ll.exam.final__2022_10_08.app.member.entity.Member;
import com.ll.exam.final__2022_10_08.app.product.entity.Product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class MyBook extends BaseEntity{
	@ManyToOne
	Member member;
	@ManyToOne
	Product product;
}
