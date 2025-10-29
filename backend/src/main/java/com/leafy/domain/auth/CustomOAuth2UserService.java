package com.leafy.domain.auth;

import com.leafy.domain.dto.SessionUser;
import com.leafy.domain.user.User;
import com.leafy.domain.user.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * OAuth2 로그인 성공 시, 사용자 정보를 가져온 후의 로직을 처리하는 서비스
 */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. 기본 OAuth2UserService를 통해 OAuth2User 정보를 가져온다.
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 2. 현재 로그인 진행 중인 서비스를 구분 (예: "kakao", "google"...)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 3. OAuth2 로그인 진행 시 키가 되는 필드 (Primary Key 역할)
        // application.yml의 user-name-attribute
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // 4. OAuth2UserService를 통해 가져온 OAuth2User의 attributes를 DTO로 변환
        com.leafy.domain.auth.OAuthAttributes attributes = com.leafy.domain.auth.OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // 5. DB에 사용자 저장 또는 업데이트
        User user = saveOrUpdate(attributes);

        // 6. 세션에 사용자 정보를 DTO(SessionUser)로 저장
        // (직렬화 가능한 DTO를 사용해야 함. User 엔티티를 직접 넣으면 안 됨)
        httpSession.setAttribute("user", new SessionUser(
                user.getKakaoId(),
                user.getNickname(),
                user.getEmail(),
                user.getProfileImageUrl()
        ));

        // 7. Spring Security의 DefaultOAuth2User 객체를 반환
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    /**
     * DB에 사용자 정보를 저장하거나, 이미 있다면 업데이트한다.
     */
    private User saveOrUpdate(com.leafy.domain.auth.OAuthAttributes attributes) {
        User user = userRepository.findByKakaoId(attributes.getKakaoId())
                // DB에 정보가 있다면, 닉네임/프로필이미지 업데이트
                .map(entity -> entity.update(attributes.getNickname(), attributes.getProfileImageUrl()))
                // DB에 정보가 없다면(최초 로그인), 새로 생성
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}