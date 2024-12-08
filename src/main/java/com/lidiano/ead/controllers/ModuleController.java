package com.lidiano.ead.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lidiano.ead.dtos.ModuleDto;
import com.lidiano.ead.models.CourseModel;
import com.lidiano.ead.models.ModuleModel;
import com.lidiano.ead.services.CourseService;
import com.lidiano.ead.services.ModuleService;
import com.lidiano.ead.specifications.SpecificationTemplate;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModuleController {

	@Autowired
	ModuleService moduleService;

	@Autowired
	CourseService courseService;

	@PostMapping("/courses/{courseId}/modules")
	public ResponseEntity<Object> saveModule(@PathVariable UUID courseId, @RequestBody @Valid ModuleDto moduleDto) {
		Optional<CourseModel> courseModelOptional = courseService.findById(courseId);

		if (!courseModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found!");
		}

		var moduleModel = new ModuleModel();
		BeanUtils.copyProperties(moduleDto, moduleModel);
		moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
		moduleModel.setCourse(courseModelOptional.get());

		return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.save(moduleModel));
	}

	@DeleteMapping("/courses/{courseId}/modules/{moduleId}")
	public ResponseEntity<Object> deleteModule(@PathVariable UUID courseId, @PathVariable UUID moduleId) {
		Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);

		if (!moduleModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course!");
		}
		moduleService.delete(moduleModelOptional.get());

		return ResponseEntity.status(HttpStatus.OK).body("Module deleted successfully!");
	}

	@PutMapping("/courses/{courseId}/modules/{moduleId}")
	public ResponseEntity<Object> updateModule(@PathVariable UUID courseId, @PathVariable UUID moduleId,
			@RequestBody @Valid ModuleDto moduleDto) {
		Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);

		if (!moduleModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course!");
		}
		var moduleModel = moduleModelOptional.get();
		BeanUtils.copyProperties(moduleDto, moduleModel);
		moduleModel.setTitle(moduleDto.getTitle());
		moduleModel.setDescription(moduleDto.getDescription());

		return ResponseEntity.status(HttpStatus.OK).body(moduleService.save(moduleModel));
	}

	@GetMapping("/courses/{courseId}/modules")
	public ResponseEntity<Page<ModuleModel>> getAllModules(@PathVariable UUID courseId,
			SpecificationTemplate.ModuleSpec spec,
			@PageableDefault(page = 0, size = 10, sort = "moduleId", direction = Direction.ASC) Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(moduleService.findAllByCourse(SpecificationTemplate.moduleCourseId(courseId).and(spec), pageable));
	}

	@GetMapping("/courses/{courseId}/modules/{moduleId}")
	public ResponseEntity<Object> getOneModule(@PathVariable UUID courseId, @PathVariable UUID moduleId) {
		Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);

		if (!moduleModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course!");
		}

		return ResponseEntity.status(HttpStatus.OK).body(moduleModelOptional.get());
	}
}
