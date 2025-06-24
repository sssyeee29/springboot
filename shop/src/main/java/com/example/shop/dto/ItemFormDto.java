package com.example.shop.dto;

import com.example.shop.constant.ItemSellStatus;
import com.example.shop.entity.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {

    private Long id;

    //설명 : null, "", " " 등 공백 문자열까지 포함해서 모두 거부
    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    //설명 : null이 아닌지만 검사함, 허용되는 값 예시: " "(공백 문자열), ""(빈 문자열)
    @NotNull(message = "가격은 필수 입력 값입니다.")
    private int price;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private int stockNumber;

    private ItemSellStatus itemSellStatus;

    //상품 저장 후 수정할 때 상품 이미지 정보를 저장하는 리스트
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    //상품의 이미지 아이디를 저장하는 리스트
    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    //ItemFormDto(자기자신) => Item 변환 // this는 ItemFormDto를 나타냄 그걸 Item으로 변환시키겠다.
    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }

    //Item 객체를 ItemFormDto로 변환하겠다.
    public static ItemFormDto of(Item item){
        return modelMapper.map(item, ItemFormDto.class);
    }
}
