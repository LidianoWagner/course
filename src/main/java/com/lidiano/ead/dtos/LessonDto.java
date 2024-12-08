package com.lidiano.ead.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LessonDto {

	@NotBlank
	private String title;

	private String description;

	@NotBlank
	private String videoUrl;
}
