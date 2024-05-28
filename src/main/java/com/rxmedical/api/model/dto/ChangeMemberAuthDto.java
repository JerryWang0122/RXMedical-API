package com.rxmedical.api.model.dto;

/**
 * [INPUT] 更改會員權限的 DTO
 *
 * @param memberId 要被修改權限的會員id
 * @param authLevel 被修改的權限
 */
public record ChangeMemberAuthDto(Integer memberId, String authLevel) {
}
