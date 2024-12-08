package com.lidiano.ead.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lidiano.ead.dtos.CourseDto;
import com.lidiano.ead.models.CourseModel;
import com.lidiano.ead.services.CourseService;
import com.lidiano.ead.specifications.SpecificationTemplate;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

	@Autowired
	CourseService courseService;

	@PostMapping
	public ResponseEntity<Object> saveCourse(@RequestBody @Valid CourseDto courseDto) {
		var courseModel = new CourseModel();
		BeanUtils.copyProperties(courseDto, courseModel);
		courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
		courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

		return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseModel));
	}

	@DeleteMapping("/{courseId}")
	public ResponseEntity<Object> deleteCourse(@PathVariable UUID courseId) {
		Optional<CourseModel> courseModelOptional = courseService.findById(courseId);

		if (!courseModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found!");
		}
		courseService.delete(courseModelOptional.get());

		return ResponseEntity.status(HttpStatus.OK).body("Course deleted successfully!");
	}

	@PutMapping("/{courseId}")
	public ResponseEntity<Object> updateCourse(@PathVariable UUID courseId, @RequestBody @Valid CourseDto courseDto) {
		Optional<CourseModel> courseModelOptional = courseService.findById(courseId);

		if (!courseModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found!");
		}
		var courseModel = courseModelOptional.get();
		BeanUtils.copyProperties(courseDto, courseModel);
		courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

		return ResponseEntity.status(HttpStatus.OK).body(courseService.save(courseModel));
	}
	
	@GetMapping
	public ResponseEntity<Page<CourseModel>> getAllCourses(SpecificationTemplate.CourseSpec spec,
			@PageableDefault(page = 0, size = 10, sort = "courseId", direction = Direction.ASC) Pageable pageable,
			@RequestParam(required = false) UUID userId) {
		
		log.info("Iniciando busca por cursos. Parâmetros - userId: {}, pageable: {}", userId, pageable);
		
		Page<CourseModel> courseModelPage;
		
		try {
			if(userId != null) {
				log.debug("Filtrando cursos pelo userId: {}", userId);
				courseModelPage = courseService.findAll(SpecificationTemplate.courseUserId(userId).and(spec), pageable);
			} else {
				log.debug("Buscando todos os cursos com os critérios especificados.");
				courseModelPage = courseService.findAll(spec, pageable);
			}
			
			if(!courseModelPage.isEmpty()) {
				log.info("Foram encontrados {} cursos. Adicionando links HATEOAS.", courseModelPage.getTotalElements());
				for(CourseModel course : courseModelPage.toList()) {
					course.add(linkTo(methodOn(CourseController.class).getOneCourse(course.getCourseId())).withSelfRel());
				}
			} else {
				log.warn("Nenhum curso encontrado para os critérios especificados.");
			}
			
			log.info("Busca concluída com sucesso.");
			return ResponseEntity.status(HttpStatus.OK).body(courseModelPage);
			
		} catch (Exception e) {
            log.error("Erro ao buscar cursos: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

	}
	
	@GetMapping("/{courseId}")
	public ResponseEntity<Object> getOneCourse(@PathVariable UUID courseId) {
		Optional<CourseModel> courseModelOptional = courseService.findById(courseId);

		if (!courseModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found!");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(courseModelOptional.get());
	}
}
