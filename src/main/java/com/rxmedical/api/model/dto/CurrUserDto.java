package com.rxmedical.api.model.dto;

/**
 * [INPUT] 當前使用者想用自己的權限做某些事情時，使用的DTO
 * @param userId 當前使用者ID
 */
public record CurrUserDto(Integer userId) {
}
