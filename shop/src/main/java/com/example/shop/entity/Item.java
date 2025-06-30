package com.example.shop.entity;

import com.example.shop.constant.ItemSellStatus;
import com.example.shop.dto.ItemFormDto;
import com.example.shop.exception.OutOfStockException;
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
    @Column(name = "item_id")
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

    //상품 업데이트 로직
    //엔티티 안에 넣는건 선호하지는 않음! 차라리 엔티티에 넣어서 하는게 더 나음
    public void updateItem(ItemFormDto itemFormDto) {
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    // 상품을 주문할 경우 상품의 재고를 감소시키는 로직
    public void removeStock(int stockNumber) {
        int restStock = this.stockNumber - stockNumber;

        if(restStock < 0) {
            throw new OutOfStockException("상품의 재고가 부족합니다." +
                    "(현재 재고 수량 : " + this.stockNumber + ")");
        }
        this.stockNumber = restStock;
    }
}
