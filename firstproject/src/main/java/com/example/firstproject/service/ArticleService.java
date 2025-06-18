package com.example.firstproject.service;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service //서비스 객체 생성
@RequiredArgsConstructor
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository; //게시글 리파지터리 객체 주입

    // 전체 데이터 조회
    public List<Article> index() {
        return articleRepository.findAll();
    }

    // 단건 데이터 조회
    public Article show(Long id) {
        log.info("1..........");
        articleRepository.findById(id).ifPresent(article -> {});
        log.info("2..........");
        Article article = articleRepository.findById(id).orElse(null);
        log.info("3...........");
        return article;
    }

    // create - insert sql 실행
    public Article create(ArticleForm dto){
        Article article = dto.toEntity();
        
        if (article.getId() != null) {
            return null;
        }

        return articleRepository.save(article);
    }


    // update - update sql 실행
    public Article update(Long id, ArticleForm dto) {
        //1. DTO -> 엔티티 변환하기
        Article article = dto.toEntity();

        //2. 타깃 조회하기
        Article target = articleRepository.findById(id).orElse(null);

        //3. 잘못된 요청 처리하기
        if (target == null || id != article.getId()) {
            log.info("id : {}, article : {}", id, article.getId());
            return null;
        }

        //4. 업데이트 및 정상 응답(200)하기
        target.patch(article);
        return articleRepository.save(target);
    }


    public Article delete(Long id) {

        // 1. 대상 찾기
        Article target = articleRepository.findById(id).orElse(null);

        // 2. 잘못된 요청 처리하기
        if (target == null) {
            return null;
        }
        // 3. 대상 삭제하기
        articleRepository.delete(target);
        return target;
    }

    // articleform으로 입력받아서 article로 변환시켜서 db저장 (입력받을때)
    // db에 저장된거 가져올땐 반대

//    @PersistenceContext
    private final EntityManager entityManager; //엔티티매니저가 commit을 시켜줌


    @Transactional
    public List<Article> createArticles(List<ArticleForm> dtos) {

        //1. dto 묶음을 엔티티 묶음으로 변환하기
        List<Article> articleList = dtos.stream().map(dto -> dto.toEntity())
                .collect(Collectors.toList());

        //2. 엔티티 묶음을 DB에 저장하기
        articleList.stream()
                .forEach(article -> articleRepository.save(article));

        //db에 저장
        entityManager.flush();


        //3. 강제 예외 발생시키기
        articleRepository.findById(-1L) //id가 -1인 데이터 찾기
                .orElseThrow(() -> new IllegalArgumentException("결제 실패!")); //찾는 데이터가 없으면 예외 발생
        //4. 결과 값 반환하기
        return articleList;
    }






}

