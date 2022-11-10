package com.phoenix.howabouttoday.member.wishlist.controller;

import com.phoenix.howabouttoday.accom.entity.Accommodation;
import com.phoenix.howabouttoday.member.entity.Member;
import com.phoenix.howabouttoday.member.wishlist.domain.WishList;
import lombok.*;

public class WishListDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    public static class RequestDto{

        private Long wishListNum;
        private Long memberNUm;
        private Long accomNum;

        /** DTO -> Entity **/
        public WishList toEntity(Member member, Accommodation accommodation){
            WishList wishList = WishList.builder()
                    .reserveNum(wishListNum)
                    .member(member)
                    .accommodation(accommodation)
                    .build();
            return wishList;
        }

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ResponseDto{

        private Long wishListNum;
        private Long memberNum;
        private Long accomNum;

        /** 찜 목록에서 보여줄 것 */
        private String accomImageFileName;
        private String accomName;
        private String accomAdress;
        private Double accomRating;
        private int reviewCount;
        private int accomPrice;


        /** Entity -> Dto **/
        public ResponseDto(WishList wishList) {
            this.wishListNum = wishList.getReserveNum();
            this.memberNum = wishList.getMember().getMemberNum();
            this.accomNum = wishList.getAccommodation().getAccomNum();
            this.accomImageFileName = wishList.getAccommodation().getAccommodationImage().get(0).getAccomOriginFilename();
            this.accomName = wishList.getAccommodation().getAccomName();
            this.accomAdress = wishList.getAccommodation().getAccomAddress1() + " " + wishList.getAccommodation().getAccomAddress2() + " " + wishList.getAccommodation().getAccomAddress3();
            this.accomRating = wishList.getAccommodation().getAccomRating();
            this.reviewCount = wishList.getAccommodation().getTotalReviewNum();
            this.accomPrice = wishList.getAccommodation().getLowPrice().intValue();

        }
    }



}
