package com.phoenix.howabouttoday.accom.entity;



import com.phoenix.howabouttoday.room.entity.Room;
import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Accommodation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accomNum;  //숙소 번호

    @Column(length = 50)
    private String accomName;//숙소 이름

    @Column(length = 50)
    private String accomTel;//숙소 전화번호

//    @Column(length = 50)
//    @Convert(converter = AccomCategoryConverter.class)
//    private AccomCategory accomCategory;//숙소 카테고리 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accom_category_num")
    private AccomCategory accomCategory;

    //    @OneToMany() 추후에 지역 카테고리 entity 만든 후에 매핑필요

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_num")
    private Region region; //숙소 지역 번호

    @Column(length = 200)
    private String accomAddress1;// 예: 서울시

    @Column(length = 200)
    private String accomAddress2; // 예: 관악구

    @Column(length = 200)
    private String accomAddress3; // 예: 나머지 도로명주소

    @Column(precision = 1, scale = 2)
    private Double accomRating;//숙소 평점

    private Integer totalReviewNum;//숙소의 리뷰 갯수

    private Integer accomWishlistCount; //즐겨찾기 버튼 갯수

    private LocalTime checkIn;
    private LocalTime checkOut;

    @Column(precision = 14, scale = 28)
    private Double latitude; //위도

    @Column(precision = 14, scale = 31)
    private Double longitude; //경도

    @Setter
    private Integer lowPrice; //숙소의 객실 최저가

    private Integer reserveRange;//예약 가능일 범위(ex. 60 → 오늘부터 +60일까지 예약 가능)


    //양방향 매핑을 위해 추가
    @OneToMany(mappedBy = "accommodation", cascade = CascadeType.ALL)
    private List<AccomImage> accommodationImage = new ArrayList<>();    //이미지 fk를 위한 매핑

    //양방향 매핑을 위해 추가
    @OneToMany(mappedBy = "accommodation", cascade = CascadeType.ALL)
    private List<Room> room = new ArrayList<>();    //이미지 fk를 위한 매핑

    //숙소의 시설
    @OneToMany(mappedBy = "accommodation", cascade = CascadeType.ALL)
    private List<AccomViewFacilities> accomViewFacilities = new ArrayList<>();

    @Builder
    public Accommodation(Long accomNum, String accomName, String accomTel, AccomCategory accomCategory, Region region, String accomAddress1,String accomAddress2,String accomAddress3, Double accomRating, Integer accomWishlistCount, Integer totalReviewNum, Double latitude, Double longitude, Integer lowPrice, Integer reserveRange, LocalTime checkIn, LocalTime checkOut) {
        this.accomNum = accomNum;
        this.accomName = accomName;
        this.accomTel = accomTel;
        this.accomCategory = accomCategory;
        this.region = region;
        this.accomAddress1 = accomAddress1;
        this.accomAddress2 = accomAddress2;
        this.accomAddress3 = accomAddress3;
        this.accomRating = accomRating;
        this.accomWishlistCount = accomWishlistCount;
        this.totalReviewNum = totalReviewNum;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lowPrice = lowPrice;
        this.reserveRange = reserveRange;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public void increaseRating(){
         Double totalRoomRating = this.room.stream().mapToDouble(room -> room.getRoomRating() * room.getRoomReviewNum()).sum();
         Integer totalRoomCount = this.room.stream().mapToInt(room -> room.getRoomReviewNum()).sum();

//        Double totalRoomRating = this.room.stream().mapToDouble(room -> room.getRoomRating()).sum();
//        Integer totalRoomCount = this.room.stream().mapToInt(room -> room.getRoomReviewNum() != 0 ? 1 : 0).sum();


        this.accomRating = Math.round((totalRoomRating / totalRoomCount.doubleValue()) * 10.0) / 10.0;
        this.totalReviewNum += 1;
    }

    //== lowPrice 업데이트가 필요할때 쓰는 메서드 ==//
    public void modifyLowPrice(Accommodation accom){

        int minPrice =1000000000;
        for (Room room : accom.getRoom()) {

            if(room.getPrice() < minPrice){
                minPrice = room.getPrice();
                accom.setLowPrice(minPrice);
            }
        }
    }


}
