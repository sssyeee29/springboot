package com.example.firstproject.service;

import com.example.firstproject.entity.Article;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 해당 클래스를 스프링 부트와 연동해 테스트
@Slf4j
class ArticleServiceTest {

    @Autowired
    ArticleService articleService; //articleService 객체 주입

    @Test // 해당 메소드가 테스트 코드임을 선언
    void index() {

        //1. 예상 데이터
        //2. 실제 데이터
        //3. 비교 및 검출

    }

    @Test
    void show_성공() {
        // id : 302 // 302	1111 시간예약
        // 1. 예상 데이터
        Article expected = new Article(4L, "7878", "7878");

        // 2. 실제 데이터
        Article article = articleService.show(4L);

        // 3. 비교 및 검증
        assertEquals(expected.toString(), article.toString());


    }

    @Test
    void show_실패() {

        // 1. 예상 데이터
        Article expected = new Article(4L, "8888", "7878");

        // 2. 실제 데이터
        Article article = articleService.show(4L);

        // 3. 비교 및 검증 => assertEquals가 두개가 같은 값인지 비교하는거
        assertEquals(expected.toString(), article.toString());

    }
}