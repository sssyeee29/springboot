package com.example.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name="order_item")
@Getter@Setter
@ToString // tostring을 ordertest에도 쓰면 결과가 안나옴(tostring문제-순환참조)
public class OrderItem extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //지연로딩 
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY) //지연로딩
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; //주문가격
    private int count; //주문수량

    public static OrderItem createOrderItem(Item item, int count){
        //주문이 들어오면 OrderItem 객체 생성
        OrderItem orderItem = new OrderItem();

        //주문할 상품과 주문 수량을 세팅
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());

        item.removeStock(count);
        return orderItem;
    }

    //주문 취소 시 주문 수량만큼 상품의 재고를 더해줌
    public void cancel(){
        this.getItem().addStock(count);
    }


    // 총 주문 금액 합계
    // 주문 가격과 주문 수량을 곱해서 해당 상품을 주문한 총 가격을 계산하는 메소드
    public int getTotalPrice(){
        return orderPrice*count;
    }
}