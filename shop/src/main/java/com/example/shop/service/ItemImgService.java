package com.example.shop.service;

import com.example.shop.entity.ItemImg;
import com.example.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;

//상품이미지 업로드하고, 상품이미지 정보를 저장
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;

    @Value("${itemImgLocation}") //yml파일에 적어둔거 불러와서 {}안에 변수에 넣어줌
    private String itemImgLocation;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {

        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일업로드
        if(!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(itemImgLocation, oriImgName,
                    itemImgFile.getBytes());
            imgUrl = "/images/item/" + imgName;
        }

        //상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);

    }

}















