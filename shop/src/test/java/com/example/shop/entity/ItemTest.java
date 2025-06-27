package com.example.shop.entity;

import com.example.shop.constant.ItemSellStatus;
import com.example.shop.repository.ItemRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryFactory;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class ItemTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    void setUp() {
        queryFactory = new JPAQueryFactory(em);
    }


    //itemNm 조회
    @Test
    public void testFindByItemNm(){
        List<Item> items = itemRepository.findByItemNm("자바");

        items.forEach(item -> log.info(item.toString()));
//        items.forEach(System.out::println);
        log.info("-------QueryDSL--------");

        //QueryDSL 이용한 테스트 코드 작성
        QItem qItem = QItem.item;

        List<Item> item2 = queryFactory
                .select(qItem)
                .from(qItem)
                .where(qItem.itemNm.eq("자바"))
                .fetch();
        item2.forEach(item -> log.info(item.toString()));
    }

    //ItemNmAndPrice
    @Test
    public void testFindByItemNmAndPrice(){
        QItem qItem = QItem.item;

        List<Item> item = queryFactory
                .selectFrom(qItem)
                .where(
                        qItem.itemNm.eq("자바"),
                        qItem.price.gt(11000)
                )
                .fetch();

        log.info(item.toString());
    }

    //OR 조건검색
    @Test
    public void testFindByItemNmOrItemDetail(){
        QItem qItem = QItem.item;

        List<Item> items = queryFactory
                .selectFrom(qItem)
                .where(
                        qItem.itemNm.contains("목요일")
                                .or(qItem.itemDetail.contains("wk"))
                )
                .fetch();

        items.forEach(item -> log.info(item.toString()));
    }

    //Enum 조건검색
    @Test
    public void testFindBySellStatus(){
        QItem qItem = QItem.item;

        List<Item> items = queryFactory
                .selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SOLD_OUT))
                .fetch();

        items.forEach(item -> log.info(item.toString()));
    }

    //동적 조건 검색(BooleanBuilder사용)
    @Test
    public void testDynamicSearch(){
        QItem qItem = QItem.item;
        BooleanBuilder builder = new BooleanBuilder();

        String searchNm = "자바";
        Integer minPrice = 10000;

        if (searchNm != null){
            builder.and(qItem.itemNm.contains(searchNm));
        }

        if(minPrice != null){
            builder.and(qItem.price.gt(minPrice));
        }

        log.info("builder : {} ", builder.toString());

        List<Item> items = queryFactory
                .selectFrom(qItem)
                .where(builder)
                .fetch();
        items.forEach(item -> log.info(item.toString()));
    }

    //정렬
    @Test
    public void testPaging(){
        QItem qItem = QItem.item;

        List<Item> items = queryFactory
                .selectFrom(qItem)
                .where(qItem.price.gt(9000))
                .orderBy(qItem.price.asc())
                .fetch();

        log.info(items.toString());
    }

    //정렬 + 페이징 처리
    @Test
    public void testPagingAndSort(){
        QItem qItem = QItem.item;

        List<Item> items = queryFactory
                .selectFrom(qItem)
                .where(qItem.price.gt(10000))
                .orderBy(qItem.price.asc())
                .offset(0) // 시작 위치 0번 인덱스부터
                .limit(2) // 최대 2건씩 조회
                .fetch();

        log.info(items.toString());
    }

    //그룹화, 집계함수(count, max, avg등)
    @Test
    public void testAggregateFunction(){
        QItem qItem = QItem.item;

        List<Tuple> fetch = queryFactory
                .select(
                        qItem.itemSellStatus,
                        qItem.price.avg()
                )
                .from(qItem)
                .groupBy(qItem.itemSellStatus) //sell, soldout 각각의 평균값을 알려줌
                .fetch();

        fetch.stream().forEach(item -> log.info(item.toString()));
    }

    //ItemImg 조회
    @Test
    public void testItemImg(){
        QItemImg qItemImg = QItemImg.itemImg;

        List<ItemImg> result = queryFactory
                .selectFrom(qItemImg)
                .where(qItemImg.repimgYn.eq("Y"))
                .fetch();
        result.forEach(item-> log.info(item.toString()));
    }

    //ItemImg, Item Join
    @Test
    public void testJoin(){
        QItem qItem = QItem.item;
        QItemImg qItemImg = QItemImg.itemImg;

        List<ItemImg> result = queryFactory
                .selectFrom(qItemImg)
                .join(qItemImg.item, qItem)
                .where(qItem.itemNm.contains("자바"))
                .fetch();

        log.info(result.toString());
    }

    //ItemImg, Item Join
    /*
        select i.*
        from item_img i
        join item t on i.item_id = t.item_id
        where t.item_nm like "%자바%";
    */

    @Test
    public void testItemJoin(){
        QItem qItem = QItem.item;
        QItemImg qItemImg = QItemImg.itemImg;

        List<ItemImg> result = queryFactory
                .selectFrom(qItemImg)
                .join(qItemImg.item, qItem)
                .where(qItem.itemNm.contains("자바"))
                .fetch();

        log.info(result.toString());
    }
}
