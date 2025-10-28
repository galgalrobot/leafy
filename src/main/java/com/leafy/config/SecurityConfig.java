package com.leafy.config;

import com.leafy.domain.auth.CustomOAuth2UserService;
import com.leafy.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Spring Security 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 보호 비활성화 (REST API 서버이므로 세션 방식이 아닌 토큰 방식 사용 권장)
                .csrf(AbstractHttpConfigurer::disable)

                // H2 콘솔을 사용한다면 FrameOptions 비활성화
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable())
                )

                // URL별 권한 관리
                .authorizeHttpRequests(authz -> authz
                        // '/api/v1/user/me' 엔드포인트는 USER 또는 ADMIN 권한 필요
                        .requestMatchers("/api/v1/user/me").hasAnyRole(Role.USER.name(), Role.ADMIN.name())

                        // '/api/v1/**' 패턴의 경로는 USER 권한 필요 (예시)
                        // .requestMatchers("/api/v1/**").hasRole(Role.USER.name())

                        // 루트, 로그인 관련 경로, 헬스 체크 등은 모두 허용
                        .requestMatchers("/", "/api/v1/user/login/kakao", "/login/**", "/oauth2/**").permitAll()

                        // 그 외 모든 요청은 인증된 사용자만 접근 가능
                        .anyRequest().authenticated()
                )

                // 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/logout") // Spring Security가 처리할 로그아웃 URL
                        .logoutSuccessUrl("/") // 로그아웃 성공 시 리다이렉트될 URL
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // JSESSIONID 쿠키 삭제
                )

                // OAuth2 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정
                        .userInfoEndpoint(userInfo -> userInfo
                                // 핵심: customOAuth2UserService에서 사용자 정보를 처리하도록 등록
                                .userService(customOAuth2UserService)
                        )
                        // 로그인 성공 시 리다이렉트될 URL (프론트엔드 주소)
                        .defaultSuccessUrl("http://localhost:3000") // TODO: 프론트엔드 URL로 변경
                        // 로그인 실패 시 리다이렉트될 URL
                        .failureUrl("/login?error=true")
                );

        return http.build();
    }
}