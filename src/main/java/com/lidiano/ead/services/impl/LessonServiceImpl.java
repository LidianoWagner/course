package com.lidiano.ead.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lidiano.ead.models.LessonModel;
import com.lidiano.ead.repositories.LessonRepository;
import com.lidiano.ead.services.LessonService;

@Service
public class LessonServiceImpl implements LessonService {

	@Autowired
	LessonRepository lessonRepository;

	@Override
	public LessonModel save(LessonModel lessonModel) {
		// TODO Auto-generated method stub
		return lessonRepository.save(lessonModel);
	}

	@Override
	public Optional<LessonModel> findLessonIntoModule(UUID moduleId, UUID lessonId) {
		// TODO Auto-generated method stub
		return lessonRepository.findLessonIntoModule(moduleId, lessonId);
	}

	@Override
	public void delete(LessonModel lessonModel) {
		// TODO Auto-generated method stub
		lessonRepository.delete(lessonModel);
	}

	@Override
	public List<LessonModel> findAllByModule(UUID moduleId) {
		// TODO Auto-generated method stub
		return lessonRepository.findAllLessonsIntoModule(moduleId);
	}

	@Override
	public Page<LessonModel> findAllByModule(Specification<LessonModel> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return lessonRepository.findAll(spec, pageable);
	}

}
