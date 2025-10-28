package com.leafy.config;

import com.leafy.domain.auth.CustomOAuth2UserService;
import com.leafy.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/",
                                "/login",
                                "/login/**",
                                "/css/**",
                                "/style/**",  // <- 이 부분이 추가되어야 함
                                "/js/**",
                                "/images/**"
                        ).permitAll()

                        // 카카오 로그인 요청 경로 허용
                        .requestMatchers("/oauth2/authorization/**").permitAll()

                        // 그 외 모든 요청은 인증된 사용자만 접근 가능
                        .anyRequest().authenticated()
                )
                // OAuth2 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        // ... (userInfoEndpoint 설정)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        // 로그인 성공 시 리다이렉트될 URL (프론트엔드 주소)
                        // "http://localhost:3000" (외부 서버) -> "/" (우리 서버의 루트 경로)로 변경
                        .defaultSuccessUrl("/")
                        // 로그인 실패 시 리다이렉트될 URL
                        .failureUrl("/?error=true")
                );

        return http.build();
    }
}
