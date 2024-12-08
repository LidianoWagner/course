package com.lidiano.ead.controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;

import com.lidiano.ead.clients.AuthUserClient;
import com.lidiano.ead.dtos.SubscriptionDto;
import com.lidiano.ead.dtos.UserDto;
import com.lidiano.ead.enums.UserStatus;
import com.lidiano.ead.models.CourseModel;
import com.lidiano.ead.models.CourseUserModel;
import com.lidiano.ead.services.CourseService;
import com.lidiano.ead.services.CourseUserService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {
	
	@Autowired
	AuthUserClient authUserClient;
	
	@Autowired
	CourseService courseService;
	
	@Autowired
	CourseUserService courseUserService;

	@GetMapping("/courses/{courseId}/users")
	public ResponseEntity<Page<UserDto>> getAllUsersByCourse(
			@PageableDefault(page = 0, size = 10, sort = "userid", direction = Direction.ASC) Pageable pageable,
			@PathVariable UUID courseId) {
		return ResponseEntity.status(HttpStatus.OK).body(authUserClient.getAllUsersByCourse(courseId, pageable));
	}
	
	@Transactional
	@PostMapping("/courses/{courseId}/users/subscription")
	public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable UUID courseId,
															   @RequestBody @Valid SubscriptionDto subscriptionDto) {
		ResponseEntity<UserDto> responseUser;
		Optional<CourseModel> courseModelOptional = courseService.findById(courseId);

		if (!courseModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found!");
		}
		
		if (courseUserService.existsByCourseAndUserId(courseModelOptional.get(), subscriptionDto.getUserid())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: subscription already exists!");
		}
		
		try {
			responseUser = authUserClient.getOneUserById(subscriptionDto.getUserid());
			if (responseUser.getBody().getUserStatus().equals(UserStatus.BLOCKED)) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("User is blocked.");
			}
		} catch (HttpStatusCodeException e) {
			if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
			}
		}
		
		CourseUserModel courseUserModel = courseUserService.save(courseModelOptional.get().convertToCourseUserModel(subscriptionDto.getUserid()));
		return ResponseEntity.status(HttpStatus.CREATED).body(courseUserModel);
	}
}
