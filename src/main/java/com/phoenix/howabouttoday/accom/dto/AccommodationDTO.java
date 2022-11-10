package com.phoenix.howabouttoday.accom.dto;

import com.phoenix.howabouttoday.accom.entity.*;
import com.phoenix.howabouttoday.room.dto.RoomDetailDTO;
import com.phoenix.howabouttoday.room.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccommodationDTO {



    private Long accomNum;  //숙소 번호
    private String accomName;//숙소 이름
    private String accomTel;//숙소 전화번호
    private AccomCategory accomCategory;
    private Region region; //숙소 지역 번호

    private String accomAddress; // 예: 나머지 도로명주소
    private Double accomRating;//숙소 평점
    private Integer totalReviewNum;//숙소의 리뷰 갯수
    private Integer accomWishlistCount; //즐겨찾기 버튼 갯수
    private LocalTime checkIn;
    private LocalTime checkOut;
    private Double latitude; //위도
    private Double longitude; //경도
    private String lowPrice; //숙소의 객실 최저가
    private Integer reserveRange;//예약 가능일 범위(ex. 60 → 오늘부터 +60일까지 예약 가능)
    private String accomOriginFileName;//숙소 이미지 대표이미지 이름
    private List<AccomImage> accommodationImage;
    private List<RoomDetailDTO> room;
    private List<AccomViewFacilities> accomViewFacilities;

    public
    AccommodationDTO(Accommodation accom) {
        this.accomNum = accom.getAccomNum();
        this.accomName = accom.getAccomName();
        this.accomTel = accom.getAccomTel();
        this.accomCategory = accom.getAccomCategory();
        this.region = accom.getRegion();
        this.accomAddress = accom.getAccomAddress1() + " " + accom.getAccomAddress2() + " " + accom.getAccomAddress3();
        this.accomRating = accom.getAccomRating();

        this.totalReviewNum = accom.getTotalReviewNum();
        this.accomWishlistCount = accom.getAccomWishlistCount();
        this.checkIn = accom.getCheckIn();
        this.checkOut = accom.getCheckOut();
        this.latitude = accom.getLatitude();
        this.longitude = accom.getLongitude();
        this.lowPrice = DecimalFormat.getInstance().format(accom.getLowPrice());
        this.reserveRange = accom.getReserveRange();
        this.accomOriginFileName = accom.getAccommodationImage().get(0).getAccomOriginFilename();
        this.accommodationImage = accom.getAccommodationImage();
        this.room = accom.getRoom().stream().map(RoomDetailDTO::new).collect(Collectors.toList());
        this.accomViewFacilities = accom.getAccomViewFacilities();
    }
}
