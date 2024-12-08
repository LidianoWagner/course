package com.lidiano.ead.services;

import java.util.UUID;

import com.lidiano.ead.models.CourseModel;
import com.lidiano.ead.models.CourseUserModel;

public interface CourseUserService {

	boolean existsByCourseAndUserId(CourseModel courseModel, UUID userid);

	CourseUserModel save(CourseUserModel courseUserModel);

}
