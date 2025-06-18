package com.example.firstproject.controller;


import com.example.firstproject.dto.MemberForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.firstproject.repository.MemberRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/members")
public class MemberController {


    private  final MemberRepository memberRepository;

    @GetMapping("")
    private String index(Model model) {
        List<Member> memberList = (List<Member>) memberRepository.findAll();
        model.addAttribute("memberList", memberList);
        return "/members/index";
    }

    @GetMapping("/new")
    public String newMember(Model model) {
        return "/members/new";
    }

    @PostMapping("/create")
    public String createMember(MemberForm form){

        //1.MemberForm -> member entity  변환
        Member member = form.toEntity();

        //2. DB 저장
        Member savedMember = memberRepository.save(member);

        return "redirect:/members";
    }

    @GetMapping("/{id}") // 데이터 조회 요청 접수
    public String show(@PathVariable("id") Long id, Model model) {

        //1. {id}값을 DB에서 꺼내오기
        Member memberEntity = memberRepository.findById(id).orElse(null);

        log.info("memberEntity: {}", memberEntity);

        //2. Entity -> Dto 변환 (생략함) => 모델에 데이터 등록하기
        //3. view 전달 (뷰 페이지 반환하기 - Model)
        model.addAttribute("member", memberEntity);
        return "members/show";
    }

}
