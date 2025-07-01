package com.example.shop.service;

import com.example.shop.dto.OrderDto;
import com.example.shop.dto.OrderHistDto;
import com.example.shop.dto.OrderItemDto;
import com.example.shop.entity.*;
import com.example.shop.repository.ItemImgRepository;
import com.example.shop.repository.ItemRepository;
import com.example.shop.repository.MemberRepository;
import com.example.shop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional //세개의 테이블이 연관되어 있어서 하나라도 오류가 나면 다 롤백 시켜야해서
@RequiredArgsConstructor
//item, OrderItem, Order 세개 테이블과 연관이 있음
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    //orderDto(맥주 2병), email(1번 테이블)
    public Long order(OrderDto orderDto, String email) {

        //주문한 상품을 조회 (맥주가 있는지)
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(()-> new EntityNotFoundException());

        //현재 로그인한 회원의 이메일 정보로 회원 정보를 조회 (1번테이블)
        Member member = memberRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();

        //주문할 상품 엔티티와 주문 수량을 이용해 주문 상품 엔티티 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        //직원에게 주문서 전달..
        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order); //주방 메세지 전달... db 저장 (Order(상위), OrderItem(하위) 테이블 저장)

        return order.getId();
    }

    //주문 목록을 조회하는 로직 (읽기 전용 트랜잭션)
    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable){

        //로그인한 유저의 이메일과 페이징 조건을 이용하여 주문 목록 조회
        List<Order> orders = orderRepository.findOrders(email, pageable);

        //유저의 주문 총 개수 구하기 (페이징 처리를 위한 totalCount)
        Long totalCount = orderRepository.countOrder(email);

        //반환할 주문 이력 DTO 리스트를 생성
        List<OrderHistDto> orderItemDtoList  = new ArrayList<>();

        //주문 리스트를 순회하면서 OrderHistDto 객체로 변환(OrderHistDto에 3가지 있는 정보가 담김)
        for(Order order : orders){
            //Order 객체를 기반으로 주문 상품들을 담는 리스트
            OrderHistDto orderHistDto = new OrderHistDto(order);

            //해당 주문에 포함된 주문 상품 목록을 가져옴
            List<OrderItem> orderItems = order.getOrderItems();

            //각 주문 상품을 순회하면서 DTO로 변환
            for(OrderItem orderItem : orderItems){
                //주문한 상품의 대표 이미지 조회
                ItemImg itemImg = itemImgRepository
                        .findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y");

                //주문 상품 정보 + 대표 이미지 URL을 담은 DTO 생성
                OrderItemDto orderItemDto = new OrderItemDto(
                        orderItem, itemImg.getImgUrl());
                //생성한 주문 상품 DTO를 주문 이력 DTO에 추가
                orderHistDto.addOrderItemDto(orderItemDto);
            }
            // 최종 완성된 주문 이력 DTO를 리스트에 추가
            orderItemDtoList.add(orderHistDto);
        }

        //주문 이력 DTO 리스트, 페이징 정보, 총 주문 수를 기반으로 Page 객체 생성 후 반환
        return new PageImpl<OrderHistDto>(orderItemDtoList , pageable, totalCount);

    }

    /*
    주문취소
     */
    //email(로그인한 사용자), orderId(주문번호)
    public boolean validateOrder(Long orderId, String email) {
        Member curMember = memberRepository.findByEmail(email);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new EntityNotFoundException());
        //email로 로그인한 회원과 주문한 사용자가 같은지 확인
        Member savedMember = order.getMember();

        if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
            return false;
        }
        return true;
    }

    // 주문 취소
    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException());

        order.cancelOrder(); //주문 취소 상태로 변경하면 변경 감지 기능에 의해 트랜잭션이 끝날 때 update 쿼리가 실행
    }
}
