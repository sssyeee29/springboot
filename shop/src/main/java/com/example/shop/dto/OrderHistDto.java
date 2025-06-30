package com.example.shop.dto;

import com.example.shop.constant.OrderStatus;
import com.example.shop.entity.Order;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
// 주문 정보를 담는 OrderHistDto
public class OrderHistDto {
    private Long orderId; //주문 아이디
    private String orderDate; //주문날짜
    private OrderStatus orderStatus; // 주문 상태

    //주문 상품 리스트
    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    public OrderHistDto(Order order){
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }

    //orderItemDto 객체를 주문 상품 리스트에 추가하는 메소드
    public void addOrderItemDto(OrderItemDto orderItemDto){
        orderItemDtoList.add(orderItemDto);
    }


}
