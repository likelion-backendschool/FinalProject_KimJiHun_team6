package com.ll.exam.final__2022_10_08.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ll.exam.final__2022_10_08.app.cart.entity.CartItem;
import com.ll.exam.final__2022_10_08.app.cart.service.CartService;
import com.ll.exam.final__2022_10_08.app.member.entity.Member;
import com.ll.exam.final__2022_10_08.app.member.repository.MemberRepository;
import com.ll.exam.final__2022_10_08.app.product.entity.Product;
import com.ll.exam.final__2022_10_08.app.product.service.ProductService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class CartServiceTests {

	@Autowired
	private ProductService productService;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private CartService cartService;

	@Test
	@DisplayName("장바구니에 담기")
	void t1() {
		Member buyer = memberRepository.findByUsername("user1").get();

		Product product1 = productService.findById(1).get();
		Product product2 = productService.findById(2).get();
		Product product3 = productService.findById(3).get();
		Product product4 = productService.findById(4).get();

		CartItem cartItem1 = cartService.addItem(buyer, product1);
		CartItem cartItem2 = cartService.addItem(buyer, product2);
		CartItem cartItem3 = cartService.addItem(buyer, product3);
		CartItem cartItem4 = cartService.addItem(buyer, product4);

		assertThat(cartItem1).isNotNull();
		assertThat(cartItem2).isNotNull();
		assertThat(cartItem3).isNotNull();
		assertThat(cartItem4).isNotNull();
	}
}