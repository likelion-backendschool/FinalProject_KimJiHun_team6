package com.example.mission.app.post.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostModifyDto {
	private Long id;
	@NotBlank
	private String subject;
	@NotBlank
	private String content;
	@NotBlank
	private String contentHtml;
}
