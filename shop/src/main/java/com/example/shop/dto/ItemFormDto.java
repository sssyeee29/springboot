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
//item클래스랑 같은 이름의 필드로 이루어져 있어야함 => modelmapper를 사용하기위해
public class ItemFormDto {

    private Long id;

    //설명 : null, "", " " 등 공백 문자열까지 포함해서 모두 거부
    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    //설명 : null이 아닌지만 검사함, 허용되는 값 예시: " "(공백 문자열), ""(빈 문자열)
    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    @NotBlank(message = "상품 내용은 필수 입력 값입니다.")
    private String itemDetail;

    private ItemSellStatus itemSellStatus;

    //상품 저장 후 수정할 때 상품 이미지 정보를 저장하는 리스트
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    //상품의 이미지 아이디를 저장하는 리스트
    private List<Long> itemImgIds = new ArrayList<>();


    //특정 객체의 데이터를 다른 종류의 객체로 복사(매핑)해주는 역할
    //외부 라이브러리로, 클래스 만들 필요 없이 이 한줄 넣어주면 사용가능 => build.gradle에 추가해서 이 한줄로도 사용가능한거!!
    private static ModelMapper modelMapper = new ModelMapper();

    //ItemFormDto(자기자신) => Item 변환 // this는 ItemFormDto를 나타냄 그걸 Item으로 변환시키겠다.
    public Item createItem(){
        return modelMapper.map(this, Item.class);
        //modelmapper에게 this(ItemFormDto객체)를 Item 클래스의 새 인스턴스로 매핑하라고 지시함
        //modelmapper는 ItemFormDto의 필드 이름과 item의 필드 이름을 비교하여 같은 이름의 필드가 있으면
        //데이터를 자동으로 복사하여 새로운 item 객체를 생성하고 반환함
    }

    //Item 객체를 ItemFormDto로 변환하겠다.
    public static ItemFormDto of(Item item){
        return modelMapper.map(item, ItemFormDto.class);
    }
}




