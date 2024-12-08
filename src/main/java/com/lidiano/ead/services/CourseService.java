package com.lidiano.ead.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.lidiano.ead.models.CourseModel;

public interface CourseService {

	void delete(CourseModel courseModel);

	CourseModel save(CourseModel courseModel);

	Optional<CourseModel> findById(UUID courseId);

	Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable);
}
