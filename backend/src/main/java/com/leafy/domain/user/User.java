package com.leafy.domain.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
// import org.springframework.context.annotation.Role; // 🚨 이 임포트가 있다면 삭제해야 한다.

// 사용자 정보 저장을 위한 JPA 엔티티
@Getter
@NoArgsConstructor
@Entity
@Table(name = "leafy_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // DB용 Primary Key

    @Column(nullable = false, unique = true)
    private String kakaoId; // 카카오에서 제공하는 고유 ID (인증키로 사용)

    @Column(nullable = false)
    private String nickname; // 사용자 닉네임

    @Column(nullable = true)
    private String email; // 사용자 이메일 (선택 동의 시)

    private String profileImageUrl; // 프로필 이미지 URL

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // ✅ com.leafy.domain.user.Role Enum 참조

    @Builder
    public User(String kakaoId, String nickname, String email, String profileImageUrl, Role role) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
    }

    // 카카오 정보 업데이트 시 사용
    public User update(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        return this;
    }
}