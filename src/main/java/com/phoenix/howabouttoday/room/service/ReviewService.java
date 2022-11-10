package com.phoenix.howabouttoday.room.service;

import com.phoenix.howabouttoday.member.dto.SessionDTO;
import com.phoenix.howabouttoday.payment.dto.OrdersDetailDTO;
import com.phoenix.howabouttoday.payment.dto.RoomReviewCreateRequestDTO;
import com.phoenix.howabouttoday.payment.dto.RoomReviewCreateResponseDTO;
import com.phoenix.howabouttoday.payment.enumType.ReviewResponseCode;
import com.phoenix.howabouttoday.room.dto.*;
import com.phoenix.howabouttoday.room.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.util.List;

public interface ReviewService {

    RoomReviewCreateResponseDTO isPossibleWrite(SessionDTO sessionDTO, RoomReviewCreateRequestDTO roomReviewCreateRequestDTO);
    List<OrdersDetailDTO> isExistOrderDetail(SessionDTO sessionDTO, Long roomNum);

    List<OrdersDetailDTO> isReservation(SessionDTO sessionDTO, Long roomNum);
    Boolean isMember(SessionDTO sessionDTO);
    Boolean isPaid(Long memberNum, Long roomNum);
    Boolean withinTwoWeeks(Long memberNum, Long roomNum);

    List<MyReviewDTO> getMemberReivew(Long memberNum);

    Slice<RoomReviewDTO> getRoomReviewList(Pageable pageable,Long roomNum);
}
