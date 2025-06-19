package com.example.shop.repository;

import com.example.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

//클래스이름, 기본키의자료형
public interface ItemRepository extends JpaRepository<Item, Long> { //crud랑 페이징처리까지 다 쓸수있음

    List<Item> findByItemNm(String itemNm);  //엔티티에 있는거랑 같아야함

    List<Item> findByItemNmLike(String itemNm);

    List<Item> findByPriceLessThan(int price);

    //JPQL => entity 객체 이용 // 더 많이 사용함(itemDetail 이부분 이름을 잘못쓰면 오류가 바로 보여서 바로 알 수 있음)
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    //nativeQuery => DB에 있는 table 이용 (item_detail 이 이름이 잘못된건 실행할때 오류가 나서 그때 잘못된걸 알 수 있음)
                    //100% sql 구문
    @Query(value = "select * from item where item_detail " +
            "like %:itemDetail% order by price desc", nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);
}
