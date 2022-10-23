package com.example.mission.base.rq;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.example.mission.member.entity.Member;
import com.example.mission.security.dto.MemberContext;
import com.example.mission.util.Ut;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequestScope
public class Rq {
	private final HttpServletRequest req;
	private final HttpServletResponse resp;
	private final MemberContext memberContext;
	@Getter
	private final Member member;
	public Rq(HttpServletRequest req, HttpServletResponse resp) {
		this.req = req;
		this.resp = resp;

		/*
		현재 로그인한 회원의 인증정보를 비교해서 들고옴
		memberContext는 기본 시큐리티 Principal에서 더 확장한 객체
		member는 현재 사용자 정보를 담아뒀다.
		*/
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getPrincipal() instanceof MemberContext) {
			this.memberContext = (MemberContext) authentication.getPrincipal();
			this.member = memberContext.getMember();
		} else {
			this.memberContext = null;
			this.member = null;
		}
	}
	public static String redirectWithMsg(String url, String msg) {
		return "redirect:" + urlWithMsg(url, msg);
	}

	public static String urlWithMsg(String url, String msg) {
		return Ut.url.modifyQueryParam(url, "msg", msgWithTtl(msg));
	}

	private static String msgWithTtl(String msg) {
		return Ut.url.encode(msg) + ";ttl=" + new Date().getTime();
	}

	public String historyBack(String msg) {
		req.setAttribute("alertMsg", msg);
		return "common/js";
	}
}
