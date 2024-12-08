package com.lidiano.ead.dtos;

import java.util.UUID;

import com.lidiano.ead.enums.UserStatus;
import com.lidiano.ead.enums.UserType;

import lombok.Data;

@Data
public class UserDto {

	private UUID userid;
	private String username;
	private String email;
	private String fullName;
	private UserStatus userStatus;
	private UserType userType;
	private String phoneNumber;
	private String CPF;
	private String imageUrl;
}
