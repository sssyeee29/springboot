package com.example.shop.repository;

import com.example.shop.constant.Role;
import com.example.shop.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void memberInsertTest(){
        Member member = Member.builder()
                .email("test@test.com")
                .name("마루")
                .address("경기도 이천시")
                .password(passwordEncoder.encode("1234"))//DB 저장할때 반드시 암호화해서 넣어야함 !!
                .role(Role.USER)
                .build();

        memberRepository.save(member);
    }

    @Test
    public void memberSelectTest(){
        Member member = memberRepository.findByEmail("test@test.com");
        assertNotNull(member); //member가 있으면 true, 없으면 false
    }
}