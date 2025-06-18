package com.example.firstproject.dto;

import com.example.firstproject.entity.Article;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class ArticleForm {
    private Long id;
    private String title;
    private String content;

    //한건데이터 처리하는 ..
    public Article toEntity() {
        return new Article(id,title,content);
    }
}
