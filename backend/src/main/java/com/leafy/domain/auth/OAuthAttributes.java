package com.leafy.domain.auth;

import com.leafy.domain.user.Role;
import com.leafy.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * OAuth2 인증을 통해 받아온 사용자의 속성(attributes)을 담는 DTO이다.
 * 어떤 소셜 로그인(카카오, 구글 등)이든 이 DTO를 사용하여 일관되게 처리한다.
 */
@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes; // OAuth2User.getAttributes() 원본
    private String nameAttributeKey; // Primary Key (여기서는 "id")
    private String kakaoId;
    private String nickname;
    private String email;
    private String profileImageUrl;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey,
                           String kakaoId, String nickname, String email, String profileImageUrl) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }

    /**
     * Provider(kakao, google...)별로 attributes를 파싱하여 OAuthAttributes 객체를 생성한다.
     *
     * @param registrationId     현재 로그인 진행 중인 서비스 (예: "kakao")
     * @param userNameAttributeName OAuth2 로그인 진행 시 키가 되는 필드값 (application.yml의 user-name-attribute)
     * @param attributes         OAuth2UserService에서 반환하는 사용자 정보
     * @return OAuthAttributes
     */
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        // 현재는 "kakao"만 지원
        if ("kakao".equals(registrationId)) {
            return ofKakao(userNameAttributeName, attributes);
        }
        // 추후 구글, 네이버 등 확장 가능
        // if ("google".equals(registrationId)) {
        //     return ofGoogle(userNameAttributeName, attributes);
        // }

        throw new IllegalArgumentException("Unsupported social login type: " + registrationId);
    }

    /**
     * 카카오 인증 정보(attributes)를 파싱한다.
     * 카카오 응답 JSON 구조:
     * {
     * "id": 123456789,
     * "kakao_account": {
     * "profile": { "nickname": "...", "profile_image_url": "..." },
     * "email": "..."
     * }
     * }
     */
    @SuppressWarnings("unchecked") // unchecked cast 경고 억제용 카카오 응답 구조는 Oauth2라는 정해진 형식을 따르기에 Map<String, Object>임을 알기 때문, 혹시 문제되면 삭제
    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        // 카카오 응답은 'kakao_account' 내부에 profile과 email이 중첩되어 있다.
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .kakaoId(String.valueOf(attributes.get("id"))) // attributes의 최상위 "id"
                .nickname((String) profile.get("nickname"))
                .profileImageUrl((String) profile.get("profile_image_url"))
                .email((String) kakaoAccount.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName) // "id"
                .build();
    }

    /**
     * OAuthAttributes DTO를 User 엔티티로 변환한다.
     * 최초 가입 시 사용된다.
     *
     * @return User 엔티티
     */
    public User toEntity() {
        return User.builder()
                .kakaoId(kakaoId)
                .nickname(nickname)
                .email(email)
                .profileImageUrl(profileImageUrl)
                .role(Role.USER) // 최초 가입 시 기본 권한은 USER
                .build();
    }
}