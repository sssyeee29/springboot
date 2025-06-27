package com.example.shop.dto;

import com.example.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 상품 관리 페이지에서 관리자가 원하는 조건으로 상품을
// 효율적으로 검색할 수 있게 도와주는 검색 조건 묶음 객체
public class ItemSearchDto {

    private String searchDateType; // 날짜 조회

    private ItemSellStatus searchSellStatus; // 판매 상태 조회

    private String searchBy; //상품 조회 시 어떤 유형으로 조회할지 : 상품명(itemNm) or 상품 등록자 아이디(createdBy)

    private String searchQuery = ""; // 조회할 검색어 저장할 변수(삼품명, 삼품등록자 ID)


}
    //searchBy가 itemNm이면 상품명 기준으로 검색, createBy일 경우 상품 등록자 아이디 기준으로 검색

