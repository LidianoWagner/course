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

import com.lidiano.ead.dtos.LessonDto;
import com.lidiano.ead.models.LessonModel;
import com.lidiano.ead.models.ModuleModel;
import com.lidiano.ead.services.LessonService;
import com.lidiano.ead.services.ModuleService;
import com.lidiano.ead.specifications.SpecificationTemplate;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonController {

	@Autowired
	LessonService lessonService;

	@Autowired
	ModuleService moduleService;

	@PostMapping("/modules/{moduleId}/lessons")
	public ResponseEntity<Object> saveLesson(@PathVariable UUID moduleId, @RequestBody @Valid LessonDto lessonDto) {
		Optional<ModuleModel> moduleModelOptional = moduleService.findById(moduleId);

		if (!moduleModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found!");
		}

		var lessonModel = new LessonModel();
		BeanUtils.copyProperties(lessonDto, lessonModel);
		lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
		lessonModel.setModule(moduleModelOptional.get());

		return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.save(lessonModel));
	}

	@DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
	public ResponseEntity<Object> deleteLesson(@PathVariable UUID moduleId, @PathVariable UUID lessonId) {
		Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);

		if (!lessonModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module!");
		}
		lessonService.delete(lessonModelOptional.get());

		return ResponseEntity.status(HttpStatus.OK).body("Lesson deleted successfully!");
	}

	@PutMapping("/modules/{moduleId}/lessons/{lessonId}")
	public ResponseEntity<Object> updateLesson(@PathVariable UUID moduleId, @PathVariable UUID lessonId,
			@RequestBody @Valid LessonDto lessonDto) {
		Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);

		if (!lessonModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module!");
		}
		var lessonModel = lessonModelOptional.get();
		BeanUtils.copyProperties(lessonDto, lessonModel);
		lessonModel.setTitle(lessonDto.getTitle());
		lessonModel.setDescription(lessonDto.getDescription());
		lessonModel.setVideoUrl(lessonDto.getVideoUrl());

		return ResponseEntity.status(HttpStatus.OK).body(lessonService.save(lessonModel));
	}

	@GetMapping("/modules/{moduleId}/lessons")
	public ResponseEntity<Page<LessonModel>> getAllLessons(@PathVariable UUID moduleId,
			SpecificationTemplate.LessonSpec spec,
			@PageableDefault(page = 0, size = 10, sort = "lessonId", direction = Direction.ASC) Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(lessonService.findAllByModule(SpecificationTemplate.LessonModuleId(moduleId).and(spec), pageable));
	}

	@GetMapping("/modules/{moduleId}/lessons/{lessonId}")
	public ResponseEntity<Object> getOneLesson(@PathVariable UUID moduleId, @PathVariable UUID lessonId) {
		Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);

		if (!lessonModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module!");
		}

		return ResponseEntity.status(HttpStatus.OK).body(lessonModelOptional.get());
	}
}
