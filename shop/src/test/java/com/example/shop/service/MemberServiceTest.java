package com.example.shop.service;

import com.example.shop.dto.MemberFormDto;
import com.example.shop.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional //이게 있으면 테스트 성공 후 다시 롤백 시킴 => db에 정보 저장안됨
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder; //password 암호화

    public Member createMember(){

        MemberFormDto memberFormDto = MemberFormDto.builder()
                .email("test@email.com")
                .name("Test")
                .address("서울시 강동구")
                .password("1234")
                .build();

        return Member.createMember(memberFormDto, passwordEncoder); // memberFormDto로 변환하고,
                                                                    // 암호화가 된 상태로 return됨
    }

    @Test
    public void saveMember(){
        Member member = createMember(); //member에는 암호화된 password랑 dto정보
        Member savedMember = memberService.save(member);

        assertEquals(savedMember.getEmail(), member.getEmail());
        assertEquals(savedMember.getRole(), member.getRole());
    }

}