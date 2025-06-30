package com.example.shop.service;

import com.example.shop.dto.OrderDto;
import com.example.shop.dto.OrderHistDto;
import com.example.shop.entity.Order;
import com.example.shop.entity.OrderItem;
import com.example.shop.repository.OrderItemRepository;
import com.example.shop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.example.shop.entity.QOrderItem.orderItem;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@WithMockUser(username = "user@user.com", roles = "ADMIN")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    @Test
    public void test(){

        String email = "user@user.com";

        OrderDto orderDto = new OrderDto();

        orderDto.setCount(5);
        orderDto.setItemId(2L);

        Long order = orderService.order(orderDto, email);

        log.info("-----order----- : {}", order); // 저장후의 아이디값

        Order savedOrder = orderRepository.findById(order)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        log.info("-----savedOrder----- : {}", savedOrder);

        savedOrder.getOrderItems().forEach(orderItem -> {log.info("OrderItem: {}", orderItem);});
    }

    @Transactional
    @Test
    public void getOrderListTest(){
        String email = "user@user.com";

        Pageable pageable = PageRequest.of(0,2);

        Page<OrderHistDto> orderHistDtoList = orderService.getOrderList(email, pageable);

        orderHistDtoList.getContent().forEach(list -> log.info("list: {}", list));
        log.info("totalCount : {}", orderHistDtoList.getTotalElements());



    }

}