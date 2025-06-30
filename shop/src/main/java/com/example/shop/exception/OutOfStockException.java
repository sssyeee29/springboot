package com.example.shop.exception;

public class OutOfStockException extends RuntimeException {

    //주문 수량이 현재 재고 수보다 클 경우 주문이 되지 않는 코드
    public OutOfStockException(String message) {
        super(message);
    }
}
