package com.ll.exam.final__2022_10_08.app.cart.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

import com.ll.exam.final__2022_10_08.app.base.rq.Rq;
import com.ll.exam.final__2022_10_08.app.cart.entity.CartItem;
import com.ll.exam.final__2022_10_08.app.cart.service.CartService;
import com.ll.exam.final__2022_10_08.app.member.entity.Member;
import com.ll.exam.final__2022_10_08.app.product.entity.Product;
import com.ll.exam.final__2022_10_08.app.product.service.ProductService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
@Slf4j
public class CartController {
	private final CartService cartService;
	private final ProductService productService;
	private final Rq rq;

	@GetMapping("/list")
	@PreAuthorize("isAuthenticated()")
	public String showItems(Model model) {
		Member buyer = rq.getMember();

		List<CartItem> items = cartService.getItemsByBuyer(buyer);

		model.addAttribute("items", items);

		return "cart/items";
	}

	@PostMapping("/add/{id}")
	@PreAuthorize("isAuthenticated()")
	public String addItem(@PathVariable long id){
		Member buyer = rq.getMember();
		Product product = productService.findById(id).get();
		CartItem cartItem = cartService.addItem(buyer, product);
		log.info("Int!!!! Here: "+String.valueOf(id));
		log.info("Hello!!!! Here: "+String.valueOf(cartItem));
		return Rq.redirectWithMsg("/product/list","상품이 추가됐습니다.");
	}

	@PostMapping("/removeItems")
	@PreAuthorize("isAuthenticated()")
	public String removeItems(String ids) {
		Member buyer = rq.getMember();

		String[] idsArr = ids.split(",");

		Arrays.stream(idsArr)
			.mapToLong(Long::parseLong)
			.forEach(id -> {
				CartItem cartItem = cartService.findItemById(id).orElse(null);

				if (cartService.buyerCanDelete(buyer, cartItem)) {
					cartService.removeItem(cartItem);
				}
			});
		return Rq.redirectWithMsg("/cart/list", "%d건의 품목을 삭제하였습니다.".formatted(idsArr.length));
	}
}