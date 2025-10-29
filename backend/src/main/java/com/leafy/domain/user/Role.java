package com.leafy.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 사용자의 권한을 관리하는 Enum
 * Spring Security에서는 권한 코드에 항상 'ROLE_' 접두사가 붙어야 한다.
 */
@Getter
@RequiredArgsConstructor
public enum Role {

    // 스프링 시큐리티 기본 역할 (ROLE_USER, ROLE_ADMIN 등)
    USER("ROLE_USER", "일반 사용자"),
    ADMIN("ROLE_ADMIN", "관리자");

    private final String key;
    private final String title;
}