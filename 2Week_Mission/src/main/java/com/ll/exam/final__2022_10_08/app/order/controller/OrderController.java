package com.ll.exam.final__2022_10_08.app.order.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper objectMapper;
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

	private final String SECRET_KEY = "test_sk_O6BYq7GWPVvj1BWNkbaVNE5vbo1d";

	@RequestMapping("/{id}/success")
	public String confirmPayment(
		@RequestParam String paymentKey, @RequestParam String orderId, @RequestParam Long amount,
		Model model) throws Exception {

		HttpHeaders headers = new HttpHeaders();
		// headers.setBasicAuth(SECRET_KEY, ""); // spring framework 5.2 이상 버전에서 지원
		headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, String> payloadMap = new HashMap<>();
		payloadMap.put("orderId", orderId);
		payloadMap.put("amount", String.valueOf(amount));

		HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

		ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
			"https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);

		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			JsonNode successNode = responseEntity.getBody();
			model.addAttribute("orderId", successNode.get("orderId").asText());
			String secret = successNode.get("secret").asText(); // 가상계좌의 경우 입금 callback 검증을 위해서 secret을 저장하기를 권장함
			return "order/success";
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
}