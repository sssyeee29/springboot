package com.example.shop.repository;

import com.example.shop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

    //대표이미지 검색
    ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn);
}
