package com.leafy.config;

import com.leafy.domain.auth.CustomOAuth2UserService;
import com.leafy.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // CustomOAuth2UserService를 주입하기 위해 추가
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService; // 첫 번째 코드에서 가져옴

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF, HTTP Basic, Form Login 비활성화 (두 번째 코드)
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)

                // 2. 세션 관리: STATELESS (두 번째 코드)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. CORS 설정 적용 (두 번째 코드)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 4. 경로별 인가 설정 (두 코드의 내용을 통합)
                .authorizeHttpRequests(authz -> authz
                        // 루트, 로그인 관련 경로, 헬스 체크 등은 모두 허용 (첫 번째 코드)
                        .requestMatchers("/", "/api/v1/user/login/kakao", "/login/**", "/oauth2/**", "/health").permitAll()
                        
                        // 특정 API에 권한 부여 (첫 번째 코드)
                        .requestMatchers("/api/v1/user/me").hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                        
                        // 그 외 모든 요청은 인증된 사용자만 접근 가능 (첫 번째 코드를 기반으로 유지)
                        .anyRequest().authenticated()
                )

                // 5. H2 콘솔 프레임 허용 (두 번째 코드를 기반으로 유지, 필요시)
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable()) // 첫 번째 코드의 H2 설정
                )

                // 6. OAuth2 로그인 설정 (첫 번째 코드에서 핵심 기능 가져옴)
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // 사용자 정보 처리 서비스 등록
                        )
                        // 로그인 성공 시 리다이렉트될 URL (프론트엔드 주소)
                        .defaultSuccessUrl("http://localhost:3000") // TODO: 프론트엔드 URL로 변경
                        .failureUrl("/login?error=true")
                );
                
                // 주의: 첫 번째 코드의 '.logout(...)' 설정은 소셜 로그인 방식과 STATELESS 구조에서는 
                // JWT 토큰 관리와 충돌할 수 있으므로 일단 제외했습니다. 
                // STATELESS에서는 클라이언트가 토큰을 삭제하는 방식으로 로그아웃을 처리하는 것이 일반적입니다.

        return http.build();
    }

    // CORS 설정 Bean (두 번째 코드에서 그대로 가져옴)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5500", "http://127.0.0.1:5500"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}