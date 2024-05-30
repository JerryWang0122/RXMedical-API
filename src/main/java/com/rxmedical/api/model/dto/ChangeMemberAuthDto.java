package com.rxmedical.api.model.dto;

/**
 * [INPUT] 更改會員權限的 DTO
 * @param userId 執行人的id
 * @param memberId 要被修改權限的會員id
 * @param authLevel 被修改的權限
 */
public record ChangeMemberAuthDto(Integer userId, Integer memberId, String authLevel) {
}
