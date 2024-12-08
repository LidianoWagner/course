package com.lidiano.ead.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubscriptionDto {

	@NotNull
	private UUID userid;
}
