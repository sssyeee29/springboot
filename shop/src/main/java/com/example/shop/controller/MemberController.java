package com.example.shop.controller;

import com.example.shop.dto.MemberFormDto;
import com.example.shop.entity.Member;
import com.example.shop.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/login")
    public String loginMember(){
        return "member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "member/memberLoginForm";
    }

    //localhost:8080/members/new
    // 회원가입 폼 제공
    @GetMapping(value = "/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, //@Valid : 유효성
                             BindingResult bindingResult, Model model) {

        //에러발생시 => 유효성 검사에서 문제가 생겼을 때
        if (bindingResult.hasErrors()) { //유효성 검사 실패시
            return "member/memberForm";
        }
        try { //오류 없이 통과되면 회원객체 생성후 정보 저장
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.save(member); //여기서 memberService에 있는 validateDuplicateMember()를 호출해서 오류메세지를 뷰에 보여줌
        }catch(IllegalArgumentException e) { //회원 생성 중 문제 생기면 예외처리
            model.addAttribute("errorMessage", e.getMessage()); //memberForm.html에 있는 errorMessage
            return "member/memberForm";
        }
        return "redirect:/";
    }


}
