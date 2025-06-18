package com.example.firstproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity // 테이블로 생성해줌
//@table(name = "article") => 테이블 이름을 입력하지 않으면 클래스 이름이 기본적으로 테이블 이름이 됨
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class Article {

    @Id // 기본키 설정 어노테이션 (기본키는 반드시 필요함)
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동생성 기능 추가(숫자 자동증가 = 시퀀스)
    private Long id;

    @Column // title 필드 선언, DB 테이블의 title열과 연결됨
    private String title;

    @Column // content 필드 선언, DB테이블의 content 열과 연결(Column 안써도 알아서 생성해줌)
    private String content;


    // 내가 수정한 내용들만 바뀌고, 수정안한건 그냥 그대로 나오게 설정하는것
    public void patch(Article article) {

        if(article.title != null){
            this.title = article.title;
        }

        if(article.content != null){
            this.content = article.content;
        }
    }
}
