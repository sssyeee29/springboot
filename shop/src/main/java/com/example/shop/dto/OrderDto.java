package com.example.shop.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//상품 상세 페이지에서 주문할 상품의 아이디와 주문수량을 전달받는 dto 클래스
public class OrderDto { 
    
    @NotNull(message = "상품 아이디는 필수 입력 값입니다.")
    private Long itemId;
    
    @Min(value = 1, message = "최소 주문 수량은 1개입니다.") //유효성검사
    @Max(value = 999, message = "최대 주문 수량은 999개입니다.") //유효성검사
    private int count;
}
