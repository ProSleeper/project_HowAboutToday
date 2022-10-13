package com.phoenix.howabouttoday.payment.service;

import com.phoenix.howabouttoday.accom.entity.AccomImage;
import com.phoenix.howabouttoday.accom.entity.Accommodation;
import com.phoenix.howabouttoday.accom.repository.AccommodationRepository;
import com.phoenix.howabouttoday.payment.AccomCategory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;


@SpringBootTest
@Transactional
class AccomodationServiceTest {

    @Autowired
    private AccommodationRepository accommodationRepository;
//    private final AccommodationImageRepository accommodationImageRepository;


    @Test
    public void 생성_매핑_테스트() {
        AccomImage image = AccomImage.builder()
                .accomOriginFilename("image0.jpg")
                .accomSaveFilename("image0.jpg")
                .build();

        AccomImage image1 = AccomImage.builder()
                .accomOriginFilename("image0.jpg")
                .accomSaveFilename("image0.jpg")
                .build();


        Accommodation newMember = Accommodation.builder()
                .accomName("보령(대천) 너울펜션")
                .accomTel("050350577805")
                .accomCategoryName(AccomCategory.PENSION)
                .regionNum(8)
                .accomAddress("충청남도 보령시 해수욕장13길 10-20")
                .accomRating(4.4)
                .accomWishlistCount(110)
                .totalReviewNum(1103)
                .latitude(36.3196)
                .longitude(126.5092)
                .lowPrice(45000)
                .reserveRange(60)
                .accommodationImage(new ArrayList<AccomImage>())
                .build();


        image.setAccommodation(newMember);
        image1.setAccommodation(newMember);
        newMember.getAccommodationImage().add(image);
        newMember.getAccommodationImage().add(image1);


        accommodationRepository.save(newMember);


        Accommodation test = accommodationRepository.findByAccomNum(newMember.getAccomNum());

        Assertions.assertThat(test.getAccomName()).isEqualTo(newMember.getAccomName());
        Assertions.assertThat(test.getAccommodationImage().size()).isEqualTo(2);
        Assertions.assertThat(test.getAccomCategoryName()).isEqualTo(newMember.getAccomCategoryName());

        System.out.println(test.getAccomName());
        System.out.println(test.getAccommodationImage().size());
        System.out.println(test.getAccomCategoryName());
    }

    @Test
    public void 장바구니에서_결제로_넘기는_테스트(){

//        Cart cart = new Cart();

    }

}