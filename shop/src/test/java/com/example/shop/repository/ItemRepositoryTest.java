package com.example.shop.repository;

import com.example.shop.constant.ItemSellStatus;
import com.example.shop.dto.ItemSearchDto;
import com.example.shop.entity.Item;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

//test에서 then 단계에서 필요한 import
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest(){
        Item item = new Item();

        //테스트에서 set 쓰려고 item클래스에서 setter 어노테이션을 씀
        item.setItemNm("테스트상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());

        Item savedItem = itemRepository.save(item);

        log.info("savedItem : {}", savedItem.toString());
    }

    public void createItemList(){

        for (int i=1; i<=10; i++) {

            Item item = new Item();

            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명"  + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());

            Item savedItem = itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest(){
        this.createItemList(); //실행하면 새로운 데이터 10개 넣음

        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");
        itemList.forEach(item -> log.info("item : {}", item.toString()));

    }

    @Test
    @DisplayName("상품명 Like 조회 테스트")
    public void findByItemNmLikeTest(){
        this.createItemList(); //실행하면 새로운 데이터 10개 넣음
                                                              //%를 넣어야지 1이 들어가있는게 다 나옴
        List<Item> itemList = itemRepository.findByItemNmLike("%테스트 상품1%");
        itemList.forEach(item -> log.info("item : {}", item.toString()));

    }

    @Test
    @DisplayName("상품가격 조회 테스트")
    public void findByPriceLessThanTest(){
        this.createItemList(); //실행하면 새로운 데이터 10개 넣음

        List<Item> itemList = itemRepository.findByPriceLessThan(10005); //10005보다 작은거 조회해라
        itemList.forEach(item -> log.info("item : {}", item.toString()));

    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetailTest(){
        this.createItemList();

        List<Item> itemList = itemRepository.findByItemDetail("설명1"); //10005보다 작은거 조회해라
        itemList.forEach(item -> log.info("item : {}", item.toString()));

    }

    @Test
    @DisplayName("nativeQuery 속성을 이용한 상품 조회 테스트")
    public void findByItemDetailByNativeTest(){
        this.createItemList();

        List<Item> itemList = itemRepository.findByItemDetailByNative("설명1"); //10005보다 작은거 조회해라
        itemList.forEach(item -> log.info("item : {}", item.toString()));

    }

    @Autowired
    private EntityManager em;

    //
    @Test
    public void getGetAdminItemPage(){

        //Given(준비) - 테스트를 위한 초기 상태 설정
        ItemSearchDto searchDto = new ItemSearchDto();
        searchDto.setSearchDateType("1w"); //전체 날짜
        //    searchDto.setItemSellStatus(ItemSellStatus.SOLD_OUT); //품절 상품
        searchDto.setSearchSellStatus(ItemSellStatus.SELL); //판매중인 상품
        //    searchDto.setSearchBy("itemNm");
        //    searchDto.setSearchQuery("옥수수깡");

        PageRequest pageRequest = PageRequest.of(0, 5);

        //When(실행) - 태스트할 동작 실행
        Page<Item> result = itemRepository.getAdminItemPage(searchDto, pageRequest);

        //Then(검증) - 실행 결과 검증
        assertThat(result.getTotalElements()).isEqualTo(8); //판매중인(sell) 상품 조회할때 내 db에 해당되는게 몇개있는지 써서 적어줘야함
        assertThat(result.getContent().size()).isEqualTo(5); //5개씩 조회
        //    assertThat(result.getContent().get(0).getItemNm()).contains("자바");

        result.getContent().forEach(item -> log.info("item : {}", item.toString()));

    }
}

















