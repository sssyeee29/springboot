package com.example.shop.entity;

import com.example.shop.constant.ItemSellStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "item") //table을 item으로 설정
@Getter
@Setter
@ToString
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 상품코드

    @Column(nullable = false, length = 50) // null허용X, 상품명 50자까지 가능
    private String itemNm; // 상품명

    @Column(nullable = false, name = "price")
    private int price; // 가격

    @Column(nullable = false)
    private int stockNumber; // 재고수량

    @Lob //대용량을 기입할때
    @Column(nullable = false)
    private String itemDetail; // 상품 상세 설명

    @Enumerated(EnumType.STRING) //enum타입 클래스랑 연결(STRING 필수!)
    private ItemSellStatus itemSellStatus; // 상품 판매상태
}
