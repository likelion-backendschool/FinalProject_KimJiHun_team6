package com.example.mission.base.rq;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.example.mission.member.entity.Member;
import com.example.mission.security.dto.MemberContext;

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

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getPrincipal() instanceof MemberContext) {
			this.memberContext = (MemberContext) authentication.getPrincipal();
			this.member = memberContext.getMember();
		} else {
			this.memberContext = null;
			this.member = null;
		}
	}

}
