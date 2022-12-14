package com.ll.exam.final__2022_10_08.app.order.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.exam.final__2022_10_08.app.base.rq.Rq;
import com.ll.exam.final__2022_10_08.app.member.entity.Member;
import com.ll.exam.final__2022_10_08.app.member.service.MemberService;
import com.ll.exam.final__2022_10_08.app.order.entity.Order;
import com.ll.exam.final__2022_10_08.app.order.exception.BuyerCanNotPayOrderException;
import com.ll.exam.final__2022_10_08.app.order.exception.BuyerCanNotSeeOrderException;
import com.ll.exam.final__2022_10_08.app.order.exception.OrderIdNotMatchedException;
import com.ll.exam.final__2022_10_08.app.order.exception.OrderNotEnoughRestCashException;
import com.ll.exam.final__2022_10_08.app.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
	private final OrderService orderService;
	private final MemberService memberService;
	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper objectMapper;
	private final Rq rq;
	@Value("${custom.toss.serverkey}")
	String SECRET_KEY;

	@GetMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public String showDetail(@PathVariable long id, Model model) {
		Order order = orderService.findForPrintById(id).get();

		Member buyer = rq.getMember();

		long restCash = memberService.getRestCash(buyer);

		if (orderService.buyerCanSee(buyer, order) == false) {
			throw new BuyerCanNotSeeOrderException();
		}

		model.addAttribute("order", order);
		model.addAttribute("actorRestCash", restCash);

		return "order/detail";
	}

	@PostConstruct
	private void init() {
		restTemplate.setErrorHandler(new ResponseErrorHandler() {
			@Override
			public boolean hasError(ClientHttpResponse response) {
				return false;
			}

			@Override
			public void handleError(ClientHttpResponse response) {
			}
		});
	}

	@RequestMapping("/{id}/success")
	public String confirmPayment(
		@PathVariable long id,
		@RequestParam String paymentKey,
		@RequestParam String orderId,
		@RequestParam Long amount,
		Model model
	) throws Exception {

		Order order = orderService.findForPrintById(id).get();

		long orderIdInputed = Long.parseLong(orderId.split("__")[1]);

		if ( id != orderIdInputed ) {
			throw new OrderIdNotMatchedException();
		}

		HttpHeaders headers = new HttpHeaders();
		// headers.setBasicAuth(SECRET_KEY, ""); // spring framework 5.2 ?????? ???????????? ??????
		headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, String> payloadMap = new HashMap<>();
		payloadMap.put("orderId", orderId);
		payloadMap.put("amount", String.valueOf(amount));

		Member buyer = rq.getMember();
		long restCash = memberService.getRestCash(buyer);
		long payPriceRestCash = order.calculatePayPrice() - amount;

		if (payPriceRestCash > restCash) {
			throw new OrderNotEnoughRestCashException();
		}

		HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

		ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
			"https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);

		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			orderService.payByTossPayments(order, payPriceRestCash);
			return Rq.redirectWithMsg("/order/" +order.getId(), "????????? ?????????????????????.");
		} else {
			JsonNode failNode = responseEntity.getBody();
			model.addAttribute("message", failNode.get("message").asText());
			model.addAttribute("code", failNode.get("code").asText());
			return "order/fail";
		}
	}

	@RequestMapping("/{id}/fail")
	public String failPayment(@RequestParam String message, @RequestParam String code, Model model) {
		model.addAttribute("message", message);
		model.addAttribute("code", code);
		return "order/fail";
	}

	@PostMapping("/{id}/payByRestCashOnly")
	@PreAuthorize("isAuthenticated()")
	public String payByRestCashOnly(@PathVariable long id) {
		Order order = orderService.findForPrintById(id).get();

		Member buyer = rq.getMember();

		long restCash = memberService.getRestCash(buyer);

		if (orderService.buyerCanPayment(buyer, order) == false) {
			throw new BuyerCanNotPayOrderException();
		}

		orderService.payByRestCashOnly(order);

		return Rq.redirectWithMsg("/order/" +order.getId(), "??????????????? ?????????????????????.");
	}

	@PostMapping("/create")
	@PreAuthorize("isAuthenticated()")
	public String makeOrder() {
		Member member = rq.getMember();
		Order order = orderService.createFromCart(member);
		return Rq.redirectWithMsg("/order/" + order.getId(), "%d??? ????????? ?????????????????????.".formatted(order.getId()));
	}

	@PostMapping("/{id}/cancel")
	@PreAuthorize("isAuthenticated()")
	public String cancelOrder(@PathVariable long id) {
		Order order = orderService.findForPrintById(id).get();
		orderService.cancel(order);
		return Rq.redirectWithMsg("/product/list","%d??? ????????? ?????????????????????.".formatted(order.getId()));
	}
}