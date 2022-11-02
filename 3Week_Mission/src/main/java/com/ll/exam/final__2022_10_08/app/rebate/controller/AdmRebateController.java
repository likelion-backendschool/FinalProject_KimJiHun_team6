package com.ll.exam.final__2022_10_08.app.rebate.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
	@ResponseBody
	public String makeData(String yearMonth) {
		rebateService.makeDate(yearMonth);
		return "성공";
	}
}