package com.lidiano.ead.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lidiano.ead.models.CourseUserModel;
import com.lidiano.ead.models.CourseModel;


public interface CourseUserRepository extends JpaRepository<CourseUserModel, UUID> {

	boolean existsByCourseAndUserId(CourseModel course, UUID userid);
}
