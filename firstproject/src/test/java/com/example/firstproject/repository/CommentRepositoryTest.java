package com.example.firstproject.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommentRepositoryTest {


    @Autowired
    private CommentRepository comment;

    @Test
    void findByArticleId() {

        comment.findByArticleId(1L)
                .forEach(System.out::println);

    }


}