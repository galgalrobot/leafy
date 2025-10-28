package com.leafy;

import org.springframework.boot.SpringApplication; // 1. import 추가
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LeafyApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeafyApplication.class, args);
    }
}
// 스프링 부트 애플리케이션의 시작점(Entry Point)이다.
// @SpringBootApplication 어노테이션이 포함되며,
// main 메소드를 실행하여 내장 톰캣(Tomcat) 서버를 구동하고 앱을 실행시킨다.