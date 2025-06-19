package com.example.shop.repository;

import com.example.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    //중복된 회원이 있는지검사하기 위해 이메일로 회원 검사
    Member findByEmail(String email);

}
