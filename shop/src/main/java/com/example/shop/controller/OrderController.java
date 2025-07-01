package com.example.shop.controller;

import com.example.shop.dto.OrderDto;
import com.example.shop.dto.OrderHistDto;
import com.example.shop.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //ItemDtl에서 주문하기 누르면 order가 동작하여 js동작하고 거기에 있는 result값이 orderId에 전달되어 나머지 실행
    @PostMapping("/order")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDto orderDto,
                                              BindingResult bindingResult, Principal principal) {

        if(bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();

            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }

            log.info("sb>>>>>>>>>> : {}", sb.toString());

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();
        Long orderId = 0L;

        try{
            orderId = orderService.order(orderDto, email);
        }catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    // 구매이력을 조회할 수 있도록 지금까지 구현한 로직을 호출하는 메소드
    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page,
                            Principal principal, Model model){

        // 한번에 가지고 올 주문의 갯수는 4
        Pageable pageable =
                PageRequest.of(page.isPresent() ? page.get() : 0, 4);


        Page<OrderHistDto> ordersHistDtoList =
                orderService.getOrderList(principal.getName(), pageable);

        //model에 주문목록을 orders라고 담아서 view(jsp/thymeleaf)에서 ${orders}로 접근가능
        model.addAttribute("orders", ordersHistDtoList);
        model.addAttribute("page",pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "order/orderHist";
    }

    //주문취소
    @PostMapping(value = "/order/{orderId}/cancel") //orderHist.html의 19번째 줄이랑 같음
    public @ResponseBody ResponseEntity cancelOrder
        (@PathVariable("orderId") Long orderId, Principal principal) {


        if (!orderService.validateOrder(orderId, principal.getName())) {
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        //주문 취소 로직 호출
        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }


}





















