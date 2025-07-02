package com.example.shop.service;

import com.example.shop.dto.CartDetailDto;
import com.example.shop.dto.CartItemDto;
import com.example.shop.entity.Cart;
import com.example.shop.entity.CartItem;
import com.example.shop.entity.Item;
import com.example.shop.entity.Member;
import com.example.shop.repository.CartItemRepository;
import com.example.shop.repository.CartRepository;
import com.example.shop.repository.ItemRepository;
import com.example.shop.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.smartcardio.CardTerminal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CartService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    //징비구니에 상품을담는 로직 작성
    public Long addCart(CartItemDto cartItemDto, String email) {
        //장바구니에 담을 상품 엔티티를 조회
        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(() -> new EntityNotFoundException());

        //현재 로그인한 회원 엔티티 조회(장바구니 주인)
        Member member = memberRepository.findByEmail(email);

        //현재 로그인한 회원의 장바구니 엔티티 조회
        Cart cart = cartRepository.findByMemberId(member.getId());
        if(cart == null){ //상품을 처음으로 장바구니에 담을 경우 해당 회원의 장바구니 엔티티 생성
            cart = Cart.createCart(member);
            cartRepository.save(cart); //cart랑 member정보 저장
        }

        //현재 상품이 장바구니에 이미 들어가 있는지 조회
        CartItem savedCartItem =
                cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        //장바구니에 이미 있던 상품일 경우 기존 수량에 현재 장바구니에 담을 수량 만큼을 더함
        if(savedCartItem != null){
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        }else { // 장바구니에 동일 상품 없으면 객체 생성해서 테이블 추가
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem); //장바구니에 들어갈 상품 저장
            return cartItem.getId();
        }

        }


    //현재 로그인 한 회원의 정보를 이용하여 장바구니에 들어있는 상품을 조회하는 로직 작성
    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email){

        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email);
        // 현재 로그인한 회원의 장바구니 엔티티를 조회
        Cart cart = cartRepository.findByMemberId(member.getId());

        //장바구니에 상품을 한 번도 안 담았을 경우 장바구니 엔티티가 없으므로 빈 리스트를 반환
        if(cart == null){
            return cartDetailDtoList;
        }

        //장바구니에 담겨있는 상품 정보를 조회
        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());

        return cartDetailDtoList;

    }

}
