package com.ll.exam.final__2022_10_08.app.rebate.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ll.exam.final__2022_10_08.app.base.rq.Rq;
import com.ll.exam.final__2022_10_08.app.rebate.entity.RebateOrderItem;
import com.ll.exam.final__2022_10_08.app.rebate.service.RebateService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/adm/rebate")
@RequiredArgsConstructor
public class AdmRebateController {
	private final RebateService rebateService;
	@GetMapping("/makeData")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String showMakeData() {
		return "adm/rebate/makeData";
	}

	@PostMapping("/makeData")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String makeData(String yearMonth) {
		rebateService.makeDate(yearMonth);
		return Rq.redirectWithMsg("/adm/rebate/rebateOrderItemList?yearMonth=" + yearMonth, "정산데이터가 성공적으로 생성되었습니다.");
	}

	@GetMapping("/rebateOrderItemList")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String showRebateOrderItemList(String yearMonth, Model model) {
		if (yearMonth == null) {
			yearMonth = "2022-10";
		}

		List<RebateOrderItem> items = rebateService.findRebateOrderItemsByPayDateIn(yearMonth);

		model.addAttribute("items", items);

		return "adm/rebate/rebateOrderItemList";
	}
}