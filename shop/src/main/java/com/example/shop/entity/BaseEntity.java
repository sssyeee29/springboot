package com.example.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
// BaseTimeEntity(등록시간, 수정시간)를 상속받고 있고
// 등록일, 수정일, 등록자, 수정자를 모두 갖는 엔티티는 이 BaseEntity를 상속받으면 됨
public class BaseEntity extends BaseTimeEntity{

    @CreatedBy
    @Column(updatable = false)
    private String createdBy; //최초의 작성자

    @LastModifiedBy
    private String modifiedBy; //마지막 수정자
}
