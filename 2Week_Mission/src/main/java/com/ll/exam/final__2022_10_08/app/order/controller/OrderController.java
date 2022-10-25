package com.ll.exam.final__2022_10_08.app.order.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.exam.final__2022_10_08.app.base.rq.Rq;
import com.ll.exam.final__2022_10_08.app.member.entity.Member;
import com.ll.exam.final__2022_10_08.app.order.entity.Order;
import com.ll.exam.final__2022_10_08.app.order.exception.BuyerCanNotSeeOrderException;
import com.ll.exam.final__2022_10_08.app.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
	private final OrderService orderService;
	private final Rq rq;

	@GetMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public String showDetail(@PathVariable long id, Model model) {
		Order order = orderService.findForPrintById(id).get();

		Member buyer = rq.getMember();

		if (orderService.buyerCanSee(buyer, order) == false) {
			throw new BuyerCanNotSeeOrderException();
		}

		model.addAttribute("order", order);

		return "order/detail";
	}
}