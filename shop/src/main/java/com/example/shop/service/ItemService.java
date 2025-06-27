package com.example.shop.service;

import com.example.shop.dto.ItemFormDto;
import com.example.shop.dto.ItemImgDto;
import com.example.shop.dto.ItemSearchDto;
import com.example.shop.dto.MainItemDto;
import com.example.shop.entity.Item;
import com.example.shop.entity.ItemImg;
import com.example.shop.repository.ItemImgRepository;
import com.example.shop.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
//상품등록 페이지 (상품등록 페이지에서 상품등록이랑 상품이미지 등록이랑 부분이 나눠져서 처리되고있음)
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final ItemImgService itemImgService;

    //                  등록화면에서 입력받은 값       등록화면에 등록한 이미지
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        //상품 등록
        Item item = itemFormDto.createItem(); //상품등록폼으로 부터 입력받은 데이터를 이용하여 item 객체를 생성
        itemRepository.save(item); // 상품데이터 저장

        //이미지 등록 - 향상된 for를 이용해서 list로 전달받은걸 하나씩 꺼내서 쓰겠다는 의미..
        for (MultipartFile multipartFile : itemImgFileList) {
            ItemImg itemImg = new ItemImg(); //itemImg 객체 생성해서

            itemImg.setItem(item);

            // 첫번째 사진이 대표이미지, 나머지는 그냥..
            if(itemImgFileList.get(0).equals(multipartFile)) {
                itemImg.setRepimgYn("Y"); // 메인페이지 보여줄 대표이미지
            }else{
                itemImg.setRepimgYn("N");
            }
            itemImgService.saveItemImg(itemImg, multipartFile); //상품의 이미지 정보를 저장
        }

        return item.getId();
    }

    /*
        Form 화면에 ItemFormDto에 있는 내용을 보여주고,
        그 내용을 수정하기 위한 코드이다.
        ItemFormDto에는 item에 있는 상품내용과 item_id에 해당하는
        ItemImg의 이미지를 조회해서 ItemFormDto에 담아서 반환
     */


    /*
        JPA가 더티체킹(변경감지)을 수행하지 않는다.
        즉, itemId 조회해서 조회한 결과를 영속계층으로 저장하고,
        그 조회한 데이터를 변경해도 update(save) 방지!!
    */


    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){

        // 등록순으로 가지고 오기 위해 상품이미지 아이디 오름차순으로 가지고 오기
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        // ItemImg(엔티티)를 ItemImgDto로 변환해서 List에 저장
        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);

            itemImgDtoList.add(itemImgDto);
        }

        //상품의 아이디를 통해 상품 엔티티를 조회
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("해당 데이터가 존재하지 않습니다."));

        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);

        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(()-> new EntityNotFoundException());

        //상품데이터 수정
        item.updateItem(itemFormDto);

        //상품이미지 수정
        List<Long> itemImgIds = itemFormDto.getItemImgIds(); //상품 이미지 아이디 리스트를 조회함

        log.info("itemImgIds : {}", itemImgIds);

        //이미지 등록 : 기존 파일 삭제 -> 새 파일 저장 -> DB 정보 갱신 과정
        for (int i=0; i<itemImgIds.size(); i++) { //상품이미지 id 리스트의 크기만큼 반복
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }

        return item.getId();
    }

    //상품조회조건과 페이지정보를 파라미터로 받아서 상품데이터를 조회하는
    @Transactional(readOnly = true) // 변경하지마라
    public Page<Item> getAdminItemPage(ItemSearchDto searchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(searchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }

}
