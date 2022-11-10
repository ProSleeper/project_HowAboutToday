package com.phoenix.howabouttoday.room.dto;


import com.phoenix.howabouttoday.room.entity.Review;
import lombok.Getter;

@Getter
public class MyReviewDTO {
    private Long reviewNum;
    private Long roomNum;
    private String roomName;

    private Long accomNum;
    private String accomName;
    private String roomOriginalFile;

    private String createdDate;
    private Double reviewRating;
    private String reviewContent;

    public MyReviewDTO(Review review) {
        this.reviewNum = review.getReviewNum();
        this.roomNum = review.getRoom().getRoomNum();
        this.roomName = review.getRoom().getRoomName();
        this.accomNum = review.getRoom().getAccommodation().getAccomNum();
        this.accomName = review.getRoom().getAccommodation().getAccomName();
        this.roomOriginalFile = review.getRoom().getRoomImageList().get(0).getRoomOriginFileName();
        this.createdDate = review.getReviewCreateDate().toString();
        this.reviewRating = review.getReviewRating();
        this.reviewContent = review.getReviewContent();
    }
}

