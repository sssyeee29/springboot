package com.example.shop.dto;

import com.example.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderItemDto {

    private String itemNm;  //상품명
    private int count;      //주문 수량
    private int orderPrice; //주문 금액
    private String imgUrl;  //상품 이미지 경로

    //객체 생성
    public OrderItemDto(OrderItem orderItem, String imgUrl){
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }

}
