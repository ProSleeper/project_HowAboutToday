package com.phoenix.howabouttoday.accom.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.phoenix.howabouttoday.accom.entity.AccomCategory;
import com.phoenix.howabouttoday.accom.entity.AccomImage;
import com.phoenix.howabouttoday.accom.entity.Accommodation;
import com.phoenix.howabouttoday.accom.entity.Region;
import com.phoenix.howabouttoday.member.wishlist.controller.WishListDto;
import com.phoenix.howabouttoday.member.wishlist.domain.WishList;
import com.phoenix.howabouttoday.room.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;


public class AccomDto {



    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponsePageDto{

        private Long accomNum;  //숙소 번호

        private String accomName;//숙소 이름

        private AccomCategoryDto.ResponseDto accomCategory;

        private String accomAddress;//숙소 주소

        private Double accomRating;//숙소 평점

        private Integer totalReviewNum;//숙소의 평점 수

        private String lowPrice; //숙소의 객실 최저가

        private AccomImageDto.ResponseDto accommodationImage;//숙소의 대표 이미지




        public ResponsePageDto(Accommodation accommodation) {
            this.accomName = accommodation.getAccomName();
            this.accomCategory = new AccomCategoryDto.ResponseDto(accommodation.getAccomCategory());
            this.accomAddress = accommodation.getAccomAddress1() +  " "
                    +  accommodation.getAccomAddress2() + " " + accommodation.getAccomAddress3();

            this.accomRating = accommodation.getAccomRating();
//            this.accomRating = accommodation.getRoom().stream()
//                    .mapToDouble(room -> room.getRoomRating())
//                    .average().getAsDouble();
//            DecimalFormat df1 = new DecimalFormat("0.0");
//            this.accomRating = Double.valueOf(df1.format(this.accomRating));

            this.totalReviewNum = accommodation.getRoom().stream()
                    .mapToInt(room -> room.getRoomReviewNum())
                    .sum();
            this.accomNum = accommodation.getAccomNum();
            this.accommodationImage = new AccomImageDto.ResponseDto(accommodation.getAccommodationImage().get(0));
            this.lowPrice = DecimalFormat.getInstance().format(accommodation.getLowPrice());

        }


    }
}
