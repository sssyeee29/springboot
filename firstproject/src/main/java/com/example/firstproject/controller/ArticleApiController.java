package com.example.firstproject.controller;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import com.example.firstproject.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor //생성자 주입 // @Autowired보다는 이게 권장
@Slf4j
@RequestMapping("/api")
public class ArticleApiController {

//    private final ArticleRepository articleRepository;

    private final ArticleService articleService;

    @GetMapping("/articles")
    public List<Article> index() {
        return articleService.index(); //findAll : 전체데이터 반환
    }

    @GetMapping("/articles/{id}")
    public Article show(@PathVariable Long id) {

        return articleService.show(id);

    }
    //
    @PostMapping("/articles")
    public ResponseEntity<Article> create(@RequestBody ArticleForm dto) {

        Article created = articleService.create(dto);

        return (created != null) ?
                ResponseEntity.status(HttpStatus.OK).body(created) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();


    }

    @PatchMapping("/articles/{id}")
    public ResponseEntity<Article> update(@PathVariable Long id, @RequestBody ArticleForm dto) {
       Article updated = articleService.update(id, dto);

       return (updated != null) ?
               ResponseEntity.status(HttpStatus.OK).body(updated) :
               ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }

    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Article> delete(@PathVariable Long id) {

        Article deleted = articleService.delete(id);

        return (deleted != null) ?
                ResponseEntity.status(HttpStatus.NO_CONTENT).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }

    //트랜잭션 연습
    //list로 묶어서 3건 데이터를 한번에
    @PostMapping("/transaction-test")
    public ResponseEntity <List<Article>> transactionTest(@RequestBody List<ArticleForm> dtos) {
        List<Article> createdList = articleService.createArticles(dtos);

        return (createdList != null) ? //null이 아니면 저장 성공
                ResponseEntity.status(HttpStatus.OK).body(createdList) : //null이 아니면 list로 값을 찍어줌
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


}
