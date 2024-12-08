package com.lidiano.ead.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lidiano.ead.models.CourseModel;
import com.lidiano.ead.models.LessonModel;
import com.lidiano.ead.models.ModuleModel;
import com.lidiano.ead.repositories.CourseRepository;
import com.lidiano.ead.repositories.LessonRepository;
import com.lidiano.ead.repositories.ModuleRepository;
import com.lidiano.ead.services.CourseService;

import jakarta.transaction.Transactional;

@Service
public class CourseServiceImpl implements CourseService {

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	ModuleRepository moduleRepository;

	@Autowired
	LessonRepository lessonRepository;

	@Transactional
	@Override
	public void delete(CourseModel courseModel) {
		List<ModuleModel> moduleModelList = moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId());
		if (!moduleModelList.isEmpty()) {
			for (ModuleModel module : moduleModelList) {
				List<LessonModel> lessonModelList = lessonRepository.findAllLessonsIntoModule(module.getModuleId());
				if (!lessonModelList.isEmpty()) {
					lessonRepository.deleteAll(lessonModelList);
				}
			}
			moduleRepository.deleteAll(moduleModelList);
		}
		courseRepository.delete(courseModel);
	}

	@Override
	public CourseModel save(CourseModel courseModel) {
		// TODO Auto-generated method stub
		return courseRepository.save(courseModel);
	}

	@Override
	public Optional<CourseModel> findById(UUID courseId) {
		// TODO Auto-generated method stub
		return courseRepository.findById(courseId);
	}

	@Override
	public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return courseRepository.findAll(spec, pageable);
	}
}
