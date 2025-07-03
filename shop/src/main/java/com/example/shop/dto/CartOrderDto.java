package com.example.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
//장바구니 페이지에서 주문할 상품 데이터를 전달할 DTO
public class CartOrderDto {

    private Long cartItemId;

    //장바구니에서 여러 개의 상품을 주문하므로 CartOrderDto 클래스가 자기 자신을 List로가지고 있도록 만듬
    private List<CartOrderDto> cartOrderDtoList;
}
