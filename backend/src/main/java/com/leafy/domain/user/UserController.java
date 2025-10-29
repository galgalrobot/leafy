package com.leafy.domain.user;

import com.leafy.dto.SessionUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; // ✅ HttpSession 임포트
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 사용자 관련 API를 처리하는 컨트롤러
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    //private final UserService userService; 음 멀 위한 코드였는지 모르겠어서 주석처러ㅣ
    private final HttpSession httpSession; // ✅ HttpSession 주입

    /**
     * 현재 로그인된 사용자 정보를 세션에서 반환한다.
     * CustomOAuth2UserService가 로그인 성공 시 세션에 "user"라는 이름으로 SessionUser를 저장한다.
     *
     * @return SessionUser (세션에 저장된 사용자 정보)
     */
    @GetMapping("/me")
    public SessionUser getCurrentUser() { // ✅ 파라미터 변경
        // 세션에서 "user" 속성을 가져온다.
        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        if (user == null) {
            // 인증되지 않은 사용자라면 null 반환
            return null;
        }

        // ❌ OAuth2User 파싱 로직 모두 삭제
        // String kakaoId = ...
        // Map<String, Object> kakaoAccount = ...

        return user; // ✅ 세션에서 가져온 SessionUser 객체를 그대로 반환
    }

    /**
     * 클라이언트가 카카오 로그인 페이지로 이동하도록 유도하는 엔드포인트이다.
     * (이하 동일)
     */
    @GetMapping("/login/kakao")
    public String kakaoLoginRedirect(HttpServletResponse response) throws Exception {
        String kakaoAuthUrl = "/oauth2/authorization/kakao";
        response.sendRedirect(kakaoAuthUrl);
        return "Redirecting to Kakao Login...";
    }

    /**
     * 로그아웃을 처리하는 엔드포인트이다.
     * (이하 동일)
     */
    @GetMapping("/logout/service")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String logoutUrl = "/logout";
        response.sendRedirect(logoutUrl);
    }
}