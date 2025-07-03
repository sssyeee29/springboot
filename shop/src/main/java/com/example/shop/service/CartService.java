package com.example.shop.service;

import com.example.shop.dto.CartDetailDto;
import com.example.shop.dto.CartItemDto;
import com.example.shop.dto.CartOrderDto;
import com.example.shop.dto.OrderDto;
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
import org.thymeleaf.util.StringUtils;

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
    private final OrderService orderService;

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

    } //end getCartList

    //장바구니 상품의 수량을 업데이트 하는 로직
    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email){
        // 현재 로그인한 회원을 조회
        Member curMember = memberRepository.findByEmail(email);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException());

        //장바구니 상품을 저장한 회원을 조회
        Member savedMember = cartItem.getCart().getMember();

        //현재 로그인한 회원과  장바구니 상품을 저장한 회원이 다를 경우 false, 같으면 true
        if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
            return false;
        }
        return true;
    }

    // 장바구니 상품의 수량을 업데이트 하는 메소드
    public void updateCartItem(Long cartItemId, int count){
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException());

        // 스냅샷하고 비교해서 값이 변경감지(더티채킹)되기 때문에
        // update 구문을 실행
        cartItem.updateCount(count);
    }

    // 상품정보에 있는 x 버튼을 클릭할 때 장바구니에 담긴 상품 삭제 로직
    public void deleteCartItem(Long cartItemId){
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException());
        cartItemRepository.delete(cartItem);
    }

    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email){

        List<OrderDto> orderDtoList = new ArrayList<>();
        
        //장바구니 페이지에서 전달받은 주문 상품 번호를 이용하여 주문 로직을 전달할 orderDto객체생성
        for(CartOrderDto cartOrderDto : cartOrderDtoList){
            log.info("cartOrderDto : {}", cartOrderDto);
            
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(() -> new EntityNotFoundException());
            
            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getCount());
            orderDtoList.add(orderDto);
        }

        //장바구니에 담은 상품을 주문하도록 주문 로직을 호출함
        //[{itemId : 1, count :2},{itemId : 2, count : 5}] => OrderService의 orders가 받아줌
        Long orderId = orderService.orders(orderDtoList, email);

        //주문이 완료되었으므로 장바구니 비우기
        for(CartOrderDto cartOrderDto : cartOrderDtoList){
            CartItem cartItem = cartItemRepository
                    .findById(cartOrderDto.getCartItemId())
                    .orElseThrow(() -> new EntityNotFoundException());
            cartItemRepository.delete(cartItem);
        }
        return orderId;
    }




}
