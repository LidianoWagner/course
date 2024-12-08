package com.lidiano.ead.services;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

public interface UtilsService {

	String createUrlGetAllUsersByCourse(UUID courseId, Pageable pageable);

}
