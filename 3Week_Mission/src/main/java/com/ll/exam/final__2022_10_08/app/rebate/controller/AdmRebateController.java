package com.ll.exam.final__2022_10_08.app.rebate.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ll.exam.final__2022_10_08.app.base.dto.RsData;
import com.ll.exam.final__2022_10_08.app.base.rq.Rq;
import com.ll.exam.final__2022_10_08.app.rebate.entity.RebateOrderItem;
import com.ll.exam.final__2022_10_08.app.rebate.service.RebateService;
import com.ll.exam.final__2022_10_08.util.Ut;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/adm/rebate")
@RequiredArgsConstructor
@Slf4j
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
		RsData makeDateRsData = rebateService.makeDate(yearMonth);
		String redirect = makeDateRsData.addMsgToUrl("redirect:/adm/rebate/rebateOrderItemList?yearMonth=" + yearMonth);
		return redirect;
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

	@PostMapping("/rebateOne/{orderItemId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String rebateOne(@PathVariable long orderItemId, HttpServletRequest req) {
		RsData rebateRsData = rebateService.rebate(orderItemId);

		String referer = req.getHeader("Referer");
		log.debug("referer : " + referer);
		String yearMonth = Ut.url.getQueryParamValue(referer, "yearMonth", "");

		String redirect = "redirect:/adm/rebate/rebateOrderItemList?yearMonth=" + yearMonth;

		redirect = rebateRsData.addMsgToUrl(redirect);

		return redirect;
	}
}