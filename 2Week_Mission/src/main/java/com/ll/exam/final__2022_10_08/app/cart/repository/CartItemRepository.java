package com.ll.exam.final__2022_10_08.app.cart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.exam.final__2022_10_08.app.cart.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	Optional<CartItem> findByBuyerIdAndProductId(long buyerId, long productId);

	boolean existsByBuyerIdAndProductId(long buyerId, long productId);
}