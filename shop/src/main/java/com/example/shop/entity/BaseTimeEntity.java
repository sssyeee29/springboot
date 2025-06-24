package com.example.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
@Setter
// 보통테이블에 등록일, 수정일, 등록자, 수정자를 모두 다 넣어주지만
// 어떤 테이블은 등록자, 수정자를 넣지 않는 테이블도 있을 수 있어서 그런 엔티티는 BaseTimeEntity만 상속받게하기위해 만드는 클래스
public class BaseTimeEntity {

    @CreatedDate // 엔티티가 생성되어 저장될 때 시간을 자동으로 지정(등록)
    @Column(updatable = false)
    private LocalDateTime regTime;

    @LastModifiedDate // 엔티티의 값을 변경할 때 시간을 자동으로 저장(수정)
    private LocalDateTime updateTime;
}
