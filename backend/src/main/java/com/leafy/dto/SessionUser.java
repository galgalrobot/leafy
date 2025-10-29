package com.leafy.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

// 사용자 세션 정보를 담는 DTO
@Getter
@NoArgsConstructor
public class SessionUser implements Serializable {
    private String kakaoId;
    private String nickname;
    private String email;
    private String profileImageUrl;

    public SessionUser(String kakaoId, String nickname, String email, String profileImageUrl) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }
}
