package com.example.shop.service;

import com.example.shop.entity.Member;
import com.example.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
//@Transactional
public class MemberService implements UserDetailsService { //service에 바로 implements해서 넣지는 않음

    private final MemberRepository memberRepository;

    public Member save(Member member){
        validateDuplicateMember(member); //email 중복 체크
        return memberRepository.save(member); //통과되면 DB 저장
    }

    //중복 회원일때 코드 실행(예외 발생)
    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){ //이미 가입된 회원
            throw new IllegalArgumentException("이미 가입된 회원입니다.");
        }
    }

    //스프링 시큐리티를 이용하여 로그인, 로그아웃 기능 구현하기
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        log.info("-----loadUserByUsername----");
        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();

    }
}
