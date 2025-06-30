package com.example.shop.repository;


import com.example.shop.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    /*
    select *
    from orders o
    join member m
    on o.member_id = m.member_id
    where m.email = "user@user.com";
     */

    //현재 로그인한 사용자의 주문 데이터를 페이징 조건에 맞춰서 조회
    @Query("select o from Order o " + //공백주의... 아님 오류남
            "where o.member.email = :email " +
            "order by o.orderDate desc")
    List<Order> findOrders(@Param("email") String email, Pageable pageable);

    /*
    select count(o.order_id)
    from orders o
    join member m
    on o.member_id = m.member_id
    where m.email = "user@user.com";
     */

    // 현재 로그인한 회원의 주문 개수가 몇 개인지 조회
    @Query("select count(o) from Order o " +
            "where o.member.email = :email")
    Long countOrder(@Param("email")String email);
}