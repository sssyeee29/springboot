package com.example.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="cart")
@Getter@Setter
@ToString
public class Cart extends BaseEntity{

    @Id
    @Column(name="cart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //회원 한명당 1개의 장바구니를 생성해주는 엔티티로직 로직(처음구매시 생김)
    public static Cart createCart(Member member) {
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }
}