package com.leafy.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// 사용자 도메인의 비즈니스 로직을 처리하는 서비스
@Service
@RequiredArgsConstructor
public class UserService {

    // private final UserRepository userRepository; -> 무슨 코드인지 모르겠어서 주석처리

    // 카카오 로그인/회원가입 로직은 CustomOAuth2UserService에서 처리됨.
    // 향후 추가될 사용자 관련 비즈니스 로직 (예: 회원 탈퇴, 정보 수정)을 이곳에 구현한다.
}
