package com.phoenix.howabouttoday.payment.service;

import com.phoenix.howabouttoday.accom.entity.Accommodation;
import com.phoenix.howabouttoday.accom.entity.AccomImage;
import com.phoenix.howabouttoday.payment.repository.AccommodationRepository_ogirin;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.ArrayList;



public class AccomodationService_origin {


    private final AccommodationRepository_ogirin accommodationRepository;
//    private final AccommodationImageRepository accommodationImageRepository;

    @Autowired
    public AccomodationService_origin(AccommodationRepository_ogirin accommodationRepository){
        this.accommodationRepository = accommodationRepository;
//        this.accommodationImageRepository = accommodationImageRepository;
    }



    public void getReadMember() {
        Accommodation test = accommodationRepository.findByAccomNum(1L);
        System.out.println(test.getAccomName());
        System.out.println(test.getAccommodationImage().size());
    }
    @Transactional
    public void createMember() {

        AccomImage image = AccomImage.builder()
                .accomOriginFilename("image0.jpg")
                .accomSaveFilename("image0.jpg")
                .build();

        AccomImage image1 = AccomImage.builder()
                .accomOriginFilename("image0.jpg")
                .accomSaveFilename("image0.jpg")
                .build();


        Accommodation newMember = Accommodation.builder().
                accomName("보령(대천) 너울펜션").
                accomTel("050350577805").
                accomCategoryNum(3).
                regionNum(8).
                accomAddress("충청남도 보령시 해수욕장13길 10-20").
                accomRating(4.4).
                accomWishlistCount(110).
                totalReviewNum(1103).
                latitude(36.3196).
                longitude(126.5092).
                lowPrice(45000).
                reserveRange(60).
                accommodationImage(new ArrayList<AccomImage>()).
                build();


        image.setAccommodation(newMember);
        newMember.getAccommodationImage().add(image);



//        accommodationImageRepository.save(image);
        accommodationRepository.save(newMember);
    }
}