package com.lidiano.ead.dtos;

import java.util.UUID;

import com.lidiano.ead.enums.CourseLevel;
import com.lidiano.ead.enums.CourseStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseDto {

	@NotBlank
	private String name;
	
	@NotBlank
	private String description;
	private String imageUrl;
	
	@NotNull
	private CourseStatus courseStatus;
	
	@NotNull
	private UUID userInstructor;
	
	@NotNull
	private CourseLevel courseLevel;
}
