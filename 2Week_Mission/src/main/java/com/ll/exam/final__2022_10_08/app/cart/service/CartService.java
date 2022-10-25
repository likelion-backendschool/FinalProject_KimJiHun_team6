package com.ll.exam.final__2022_10_08.app.cart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.exam.final__2022_10_08.app.cart.entity.CartItem;
import com.ll.exam.final__2022_10_08.app.cart.repository.CartItemRepository;
import com.ll.exam.final__2022_10_08.app.member.entity.Member;
import com.ll.exam.final__2022_10_08.app.product.entity.Product;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
	private final CartItemRepository cartItemRepository;

	public CartItem addItem(Member buyer, Product product) {
		CartItem oldCartItem = cartItemRepository.findByBuyerIdAndProductId(buyer.getId(), product.getId()).orElse(null);

		if (oldCartItem != null) {
			return oldCartItem;
		}

		CartItem cartItem = CartItem.builder()
			.buyer(buyer)
			.product(product)
			.build();

		cartItemRepository.save(cartItem);

		return cartItem;
	}

	@Transactional
	public boolean removeItem(Member buyer, Product product) {
		CartItem oldCartItem = cartItemRepository.findByBuyerIdAndProductId(buyer.getId(), product.getId()).orElse(null);

		if (oldCartItem != null) {
			cartItemRepository.delete(oldCartItem);
			return true;
		}

		return false;
	}

	public boolean hasItem(Member buyer, Product product) {
		return cartItemRepository.existsByBuyerIdAndProductId(buyer.getId(), product.getId());
	}
}