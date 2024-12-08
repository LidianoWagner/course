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
import com.lidiano.ead.models.ModuleModel;
import com.lidiano.ead.repositories.LessonRepository;
import com.lidiano.ead.repositories.ModuleRepository;
import com.lidiano.ead.services.ModuleService;

import jakarta.transaction.Transactional;

@Service
public class ModuleServiceImpl implements ModuleService {

	@Autowired
	ModuleRepository moduleRepository;

	@Autowired
	LessonRepository lessonRepository;

	@Transactional
	@Override
	public void delete(ModuleModel moduleModel) {
		List<LessonModel> lessonModelList = lessonRepository.findAllLessonsIntoModule(moduleModel.getModuleId());
		if (!lessonModelList.isEmpty()) {
			lessonRepository.deleteAll(lessonModelList);
		}
		moduleRepository.delete(moduleModel);

	}

	@Override
	public ModuleModel save(ModuleModel moduleModel) {
		// TODO Auto-generated method stub
		return moduleRepository.save(moduleModel);
	}

	@Override
	public Optional<ModuleModel> findModuleIntoCourse(UUID courseId, UUID moduleId) {
		// TODO Auto-generated method stub
		return moduleRepository.findModuleIntoCourse(courseId, moduleId);
	}

	@Override
	public List<ModuleModel> findAllByCourse(UUID courseId) {
		// TODO Auto-generated method stub
		return moduleRepository.findAllModulesIntoCourse(courseId);
	}

	@Override
	public Optional<ModuleModel> findById(UUID moduleId) {
		// TODO Auto-generated method stub
		return moduleRepository.findById(moduleId);
	}

	@Override
	public Page<ModuleModel> findAllByCourse(Specification<ModuleModel> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return moduleRepository.findAll(spec, pageable);
	}
}
