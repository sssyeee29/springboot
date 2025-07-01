package com.example.shop.entity;

import com.example.shop.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter@Setter
@ToString
public class Order extends BaseEntity {

    @Id
    @GeneratedValue//(strategy = GenerationType.IDENTITY) //id 하나씩 증가하라고 넣음
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //지연로딩
    @JoinColumn(name = "member_id") //외래키로 member_id 컬럼을 사용
    private Member member;

    //mappedBy = "order" -> OrderItem 엔티티의 order 필드와 매핑
    //cascade= All -> 주문 저장 시 관련된 주문상품도 함께 저장
    // orphanRemoval = true -> orderItems에서 제거된 주문상품은 자동으로 삭제
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)

    private List<OrderItem>  orderItems = new ArrayList<>();

    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문상태

    //주문 상품 정보들을 담아둠(주문을 많이 할 수도->orderItem엔티티를 매개변수로
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this); //orderItem에 order에 넣는거
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList){
        Order order = new Order();
        order.setMember(member); //상품을 주문한 회원 정보 세팅

        for(OrderItem orderItem : orderItemList){
            order.addOrderItem(orderItem);
        }

        order.setOrderStatus(OrderStatus.ORDER); //주문상태를 "order"로 세팅
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //orderItem에 있는 getTotalPrice로 계산
    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    //Item 클래스 주문 취소 시 주문 수량을 상품의 재고에 더해주는 로직과
    //주문 상태를 취소 상태로 바꿔주는 메소드
    public void cancelOrder(){
        this.orderStatus = OrderStatus.CANCEL;

        for(OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }


}