package com.ll.exam.final__2022_10_08.app.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.exam.final__2022_10_08.app.cart.entity.CartItem;
import com.ll.exam.final__2022_10_08.app.cart.service.CartService;
import com.ll.exam.final__2022_10_08.app.member.entity.Member;
import com.ll.exam.final__2022_10_08.app.member.service.MemberService;
import com.ll.exam.final__2022_10_08.app.mybook.service.MyBookService;
import com.ll.exam.final__2022_10_08.app.order.entity.Order;
import com.ll.exam.final__2022_10_08.app.order.entity.OrderItem;
import com.ll.exam.final__2022_10_08.app.order.repository.OrderRepository;
import com.ll.exam.final__2022_10_08.app.product.entity.Product;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
	private final MemberService memberService;
	private final CartService cartService;
	private final MyBookService myBookService;
	private final OrderRepository orderRepository;

	@Transactional
	public Order createFromCart(Member buyer) {
		List<CartItem> cartItems = cartService.getItemsByBuyer(buyer);

		List<OrderItem> orderItems = new ArrayList<>();

		for (CartItem cartItem : cartItems) {
			Product product = cartItem.getProduct();

			if (product.isOrderable()) {
				orderItems.add(new OrderItem(product));
			}

			cartService.removeItem(cartItem);
		}

		return create(buyer, orderItems);
	}

	@Transactional
	public Order create(Member buyer, List<OrderItem> orderItems) {
		Order order = Order
			.builder()
			.buyer(buyer)
			.build();

		for (OrderItem orderItem : orderItems) {
			order.addOrderItem(orderItem);
		}

		// 주문 품목으로부터 이름 생성
		order.makeName();

		orderRepository.save(order);

		return order;
	}

	@Transactional
	public void payByRestCashOnly(Order order) {
		Member buyer = order.getBuyer();
		long restCash = buyer.getRestCash();

		int payPrice = order.calculatePayPrice();

		if (payPrice > restCash) {
			throw new RuntimeException("예치금이 부족합니다.");
		}

		memberService.addCash(buyer, payPrice * -1, "주문__%d__사용__예치금".formatted(order.getId()));

		order.setPaymentDone();
		myBookService.applyItem(buyer, order.getOrderItems());
		orderRepository.save(order);
	}

	@Transactional
	public void refund(Order order) {
		int payPrice = order.getPayPrice();
		memberService.addCash(order.getBuyer(), payPrice, "주문__%d__환__예치금".formatted(order.getId()));

		order.setRefundDone();
		orderRepository.save(order);
	}

	public Optional<Order> findForPrintById(long id) {
		return findById(id);
	}

	private Optional<Order> findById(long id) {
		return orderRepository.findById(id);
	}

	public boolean buyerCanSee(Member buyer, Order order) {
		return buyer.getId().equals(order.getBuyer().getId());
	}
	@Transactional
	public void payByTossPayments(Order order, long useRestCash) {
		Member buyer = order.getBuyer();
		int payPrice = order.calculatePayPrice();

		long pgPayPrice = payPrice - useRestCash;
		memberService.addCash(buyer, pgPayPrice, "주문__%d__충전__토스페이먼츠".formatted(order.getId()));
		memberService.addCash(buyer, pgPayPrice * -1, "주문__%d__사용__토스페이먼츠".formatted(order.getId()));

		if ( useRestCash > 0 ) {
			memberService.addCash(buyer, useRestCash * -1, "주문__%d__사용__예치금".formatted(order.getId()));
		}

		order.setPaymentDone();
		myBookService.applyItem(buyer, order.getOrderItems());
		orderRepository.save(order);
	}

	public boolean buyerCanPayment(Member actor, Order order) {
		return buyerCanSee(actor, order);
	}
}