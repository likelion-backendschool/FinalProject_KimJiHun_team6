package com.example.mission.security;

import static org.springframework.security.config.http.SessionCreationPolicy.*;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(
		HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {
		http.authorizeRequests(
				authorizeRequests -> authorizeRequests
					.antMatchers("/member/login", "/member/join")
					.permitAll()
					.anyRequest()
					.authenticated()
			)
			.cors().disable()
			.csrf().disable()
			.formLogin()
			.loginPage("/member/login")
			.defaultSuccessUrl("/")
			.and()
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
			.logoutSuccessUrl("/");

		return http.build();
	}
}
