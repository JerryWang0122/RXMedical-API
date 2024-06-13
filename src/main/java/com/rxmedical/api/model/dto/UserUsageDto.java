package com.rxmedical.api.model.dto;

public record UserUsageDto(
					String dept, 
					String name, 
					String authLevel, 
					String jwt) {
}
