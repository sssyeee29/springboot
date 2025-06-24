package com.example.shop.entity;

import com.example.shop.constant.Role;
import com.example.shop.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter @Setter
@ToString
@Table(name = "member")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseEntity { //4개의 정보가 자동으로 들어감

    //form화면에서 controller 통해서 MemberFormDto로 받아서 serive 통해서 repository통해서(?) 엔티티로 변환후 DB에 저장되는거
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true) // 이메일 중복 불가
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    //MemberFormDTO -> member entity로 변환한 것 why? DB에 저장하기 위해서
    public static Member createMember(MemberFormDto memberFormDto,
                                      PasswordEncoder passwordEncoder) {
        //password 암호화
//        String password = passwordEncoder.encode(memberFormDto.getPassword());
        return Member.builder()
                .name(memberFormDto.getName())
                .email(memberFormDto.getEmail())
                .password(passwordEncoder.encode(memberFormDto.getPassword())) //encode를 사용해 password 암호화
                .address(memberFormDto.getAddress())
                .role(Role.ADMIN) //권한정보 임시로 넣은거
                .build();
    }

}

