package com.lidiano.ead.services.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lidiano.ead.models.CourseModel;
import com.lidiano.ead.models.CourseUserModel;
import com.lidiano.ead.repositories.CourseUserRepository;
import com.lidiano.ead.services.CourseUserService;

@Service
public class CourseUserServiceImpl implements CourseUserService {

	@Autowired
	private CourseUserRepository courseUserRepository;

	@Override
	public boolean existsByCourseAndUserId(CourseModel courseModel, UUID userid) {
		// TODO Auto-generated method stub
		return courseUserRepository.existsByCourseAndUserId(courseModel, userid);
	}

	@Override
	public CourseUserModel save(CourseUserModel courseUserModel) {
		// TODO Auto-generated method stub
		return courseUserRepository.save(courseUserModel);
	}
}
