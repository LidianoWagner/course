package com.lidiano.ead.clients;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.lidiano.ead.dtos.ResponsePageDto;
import com.lidiano.ead.dtos.UserDto;
import com.lidiano.ead.services.UtilsService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class AuthUserClient {
	
	@Value("${ead.api.url.authuser}")
	String BASE_URL_USER;

	@Autowired
    private RestTemplate restTemplate;
	
	@Autowired
	private UtilsService utilsService;

    public Page<UserDto> getAllUsersByCourse(UUID courseId, Pageable pageable) {
    	String url = utilsService.createUrlGetAllUsersByCourse(courseId, pageable);
        log.debug("Requesting URL: {}", url);

        try {
            ParameterizedTypeReference<ResponsePageDto<UserDto>> responseType =
                new ParameterizedTypeReference<ResponsePageDto<UserDto>>() {};
            ResponseEntity<ResponsePageDto<UserDto>> response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);

            List<UserDto> users = response.getBody().getContent();
            return new PageImpl<>(users, pageable, response.getBody().getTotalElements());
        } catch (HttpStatusCodeException e) {
            log.error("Error fetching users for courseId {}: {}", courseId, e.getMessage());
            return Page.empty(pageable);
        }
    }
    
    public ResponseEntity<UserDto> getOneUserById(UUID userid) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL_USER)
                                        .path("/users/{userid}")  // Usando o parâmetro {userid} na URL
                                        .buildAndExpand(userid)    // Expande o parâmetro userid na URL
                                        .toUriString();           // Converte para string
        
        return restTemplate.exchange(url, HttpMethod.GET, null, UserDto.class);
    }

}
