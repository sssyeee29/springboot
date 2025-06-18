package com.example.firstproject.controller;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.dto.CommentDto;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import com.example.firstproject.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/articles")
public class ArticleController {

    @Autowired // 의존성 주입
    private ArticleRepository articleRepository; // articleRepository 객체선언

    @Autowired
    private CommentService commentService;

    @GetMapping("/new")
    public String newArticleForm() {
        log.info("New article form");
        return "articles/new";
    }

    @PostMapping("/create")
    public String createArticle(ArticleForm form) { //DTO에 있는 ArticleForm
        log.info("New article created");
        log.info("articleForm: {}", form);

        // 1. DTO를 엔티티로 변환
        Article article = form.toEntity();

        // 2. 리파지터리로 엔티티를 DB에 저장
        Article saved = articleRepository.save(article); // article 엔티티를 저장해 saved 객체에 반환
        log.info("Saved: {}", saved);
        return "redirect:/articles/" + saved.getId(); // id값 가져오기 위해서 saved 객체 이용
    }

    @GetMapping("/{id}") // 데이터 조회 요청 접수
    public String show(@PathVariable("id") Long id, Model model) {
        log.info("Show article");

        //1. {id}값을 DB에서 꺼내오기
        Article articleEntity = articleRepository.findById(id).orElse(null);
        List<CommentDto> comments = commentService.comments(id);

        log.info("articleEntity: {}", articleEntity);

        //2. Entity -> Dto 변환 (생략함) => 모델에 데이터 등록하기
        model.addAttribute("article", articleEntity);
        model.addAttribute("comments", comments); // 댓글 목록 모델에 등록
        //3. view 전달 (뷰 페이지 반환하기 - Model)

        return "articles/show";
    }

    @GetMapping("")
    public String index(Model model) {
        //1. 모든 데이터 가져오기
        List<Article> articleEntityList = articleRepository.findAll();

        articleEntityList.forEach(list -> {log.info("list: {}", list);});
        //2. 모델에 데이터 등록하기
        model.addAttribute("articleList", articleEntityList);

        //3. 뷰 페이지 설정하기
        return "articles/index";
    }

    //localhost:8080/articles/2/edit => 이렇게 요청하면 edit() 메소드가 응답
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id")Long id, Model model){ //id를 매개변수로 받아오기

        //1. 수정할 데이터 가져오기 (Optional은 null값 있을때 어떻게 할건지 물어보는것)
        Article articleEntity = articleRepository.findById(id).orElse(null);

        //2. 모델에 데이터 등록하기
        model.addAttribute("article", articleEntity);

        return "articles/edit"; // 뷰 페이지 설정하기
    }

    @PostMapping("/update")
    public String updateArticle(ArticleForm form) {

        log.info("Update article : {}", form);

        //1. DTO(form)를 엔티티(articleEntity)로 변환하기
        Article articleEntity = form.toEntity();

        //2. 엔티티를 DB에 저장하기
        //2-1. DB에서 기존 데이터 가져오기 (DB에서 데이터 찾아서 Article target에 데이터 저장)
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);

        //2-2. 기존 데이터 값을 갱신하기
        if (target != null){
            articleRepository.save(articleEntity); //엔티티를 DB에 저장(갱신)-> JPA에는 update명령이 따로없음
        }

        //3. 수정 결과 페이지로 리다이렉트하기

        return "redirect:/articles/" + articleEntity.getId(); //이렇게 요청보내면 {{id}}만 있는 get요청이 받음
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id")Long id, RedirectAttributes redirectAttributes){ //한번만 쓰고 사라지게 하기위해

        //1. 삭제할 데이터 가져오기
        Article target = articleRepository.findById(id).orElse(null);

        //2. 대상 엔티티 삭제하기
        if (target != null){
            articleRepository.delete(target);
            redirectAttributes.addFlashAttribute("msg", "삭제되었습니다."); //삭제된 경우에만 나타나게
        }

        //3. 결과 페이지로 리다이렉트 하기
        return "redirect:/articles";
    }
}
