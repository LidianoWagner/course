package com.lidiano.ead.services.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.lidiano.ead.services.UtilsService;

@Service
public class UtilsServiceImpl implements UtilsService {

	@Value("${ead.api.url.authuser}")
	String BASE_URL;
	
	@Override
	public String createUrlGetAllUsersByCourse(UUID courseId, Pageable pageable) {
		// TODO Auto-generated method stub
		return UriComponentsBuilder.fromHttpUrl(BASE_URL)
	            .path("/users")
	            .queryParam("courseId", courseId)
	            .queryParam("page", pageable.getPageNumber())
	            .queryParam("size", pageable.getPageSize())
	            .queryParam("sort", pageable.getSort().toString().replaceAll(": ", ","))
	            .toUriString();
	}

}
