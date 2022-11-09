package com.phoenix.howabouttoday.room.entity;

import com.phoenix.howabouttoday.accom.entity.Accommodation;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long roomNum; //객실 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="accommodation_accomNum", referencedColumnName = "accomNum")
    private Accommodation accommodation;

    @Column(length = 50)
    private String roomName; //객실 이름

    @Column(nullable = false)
    private Integer defaultGuest;//최소 인원

    @Column(nullable = false)
    private Integer maxGuest;//최대 인원

    @Column//(nullable = false)
    private LocalDate stayStartDate;//객실 이용 시작일

    @Column//(nullable = false)
    private LocalDate stayEndDate;//객실 이용 종료일

    @Column(nullable = false)
    private Integer price;//객실 가격

    @Column(precision = 1, scale = 2)
    private Double roomRating;//숙소 평점

    private Integer roomReviewNum;//숙소의 평점 수

    @Column(nullable = false)
    private String roomInfo;//객실 정보

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RoomImage> roomImageList = new ArrayList<>();

    //양방향 매핑을 위해 추가
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AvailableDate> availableDate = new ArrayList<>();

    //양방향 매핑을 위해 추가
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    @Builder
    public Room(String roomName,int defaultGuest,int maxGuest, Double roomRating,Integer roomReviewNum,Integer price, String roomInfo, Accommodation accommodation, LocalDate stayEndDate, LocalDate stayStartDate) {
        this.roomRating = roomRating;
        this.roomReviewNum = roomReviewNum;
        this.roomName = roomName;
        this.defaultGuest = defaultGuest;
        this.maxGuest = maxGuest;
        this.price = price;
        this.roomInfo = roomInfo;
        this.accommodation = accommodation;
        this.stayStartDate = stayStartDate;
        this.stayEndDate = stayEndDate;
    }

    private void addReviewCount(){
        this.roomReviewNum += 1;
    }

    public void calculateRating(Double inputRating){
        addReviewCount();

        this.roomRating = Math.round(((this.roomRating * (getRoomReviewNum() - 1) + inputRating) / getRoomReviewNum()) * 10.0) / 10.0;

    }
}


