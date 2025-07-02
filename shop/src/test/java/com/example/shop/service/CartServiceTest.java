package com.example.shop.service;

import com.example.shop.dto.CartItemDto;
import com.example.shop.entity.Cart;
import com.example.shop.entity.CartItem;
import com.example.shop.entity.Member;
import com.example.shop.repository.CartItemRepository;
import com.example.shop.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@WithMockUser(username = "user@user.com", roles = "ADMIN")
class CartServiceTest {

    @Autowired
    private CartService cartService;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void addCartTest(){

        //given : 저장하기 위한 준비과정
        String email = "user@user.com";

        CartItemDto cartItemDto = new CartItemDto();

        cartItemDto.setItemId(2L);
        cartItemDto.setCount(5);

        //when : 실제로 메뉴를 저장하는 단계
        Long result = cartService.addCart(cartItemDto, email);

        //then : 저장이 됐는지
        CartItem cartItem = cartItemRepository.findById(result)
                .orElseThrow(()->new EntityNotFoundException());

        log.info("-----result-----: {}", result);

        assertEquals(cartItemDto.getItemId(), result);
    }

}