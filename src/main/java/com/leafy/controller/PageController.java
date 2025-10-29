package com.leafy.controller;

import com.leafy.dto.SessionUser; // SessionUser 임포트
import jakarta.servlet.http.HttpSession; // HttpSession 임포트
import lombok.RequiredArgsConstructor; //  RequiredArgsConstructor 추가
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {


    /**
     * [수정됨]
     * 사용자가 "http://localhost:8080/" (서버 주소)로 바로 접속했을 때
     * 1. 세션에 "user" 정보가 있으면 -> 메인 페이지("main.html" 등)로 이동
     * 2. 세션에 정보가 없으면 -> 로그인 페이지(/login)로 이동
     */

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // "templates/login.html"을 의미
    }

    /**
     * 루트 경로 ("/")
     * 1. 세션O -> 메인 페이지
     * 2. 세션X -> 로그인 페이지로 리다이렉트
     */
    @GetMapping("/")
    public String root(HttpSession session) { // 메소드 파라미터로 받는 것이 안전함
        SessionUser user = (SessionUser) session.getAttribute("user");

        if (user != null) {
            return "main"; // "templates/main.html"을 의미
        } else {
            return "login";
        }
    }
}