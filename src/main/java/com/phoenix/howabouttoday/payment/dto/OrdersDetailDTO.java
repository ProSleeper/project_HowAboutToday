
/**
 * 하나의 숙소에 대한 정보를 담고 있는 DTO
 * OrderDetail 엔티티와 거의 1:1 관계
 *
 */

package com.phoenix.howabouttoday.payment.dto;

import com.phoenix.howabouttoday.reserve.domain.Reservation.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.TextStyle;
import java.util.Locale;

@Getter
@Builder
@AllArgsConstructor
public class OrdersDetailDTO {
    private Long ordersDetailNum;
    private String accomType;
    private String accomTypeIcon;
    private String accomName;
    private String accomRegion;
    private String orderDate;
    private String usePeriod;
    private String startDate;
    private String endDate;
    private String startWeek;
    private String endWeek;
    private String price;
    private String usedStatus;
    private String roomName;
    private String roomNum;
    private String checkIn;
    private String checkOut;
    private String roomOriginFileName;

    public OrdersDetailDTO(Long ordersDetailNum, String accomType, String accomTypeFilePath, String accomName, String accomRegion, String orderDate, String usePeriod, String startDate, String endDate, String startWeek, String endWeek, String price, String usedStatus, String roomName, String roomNum, String checkIn, String checkOut) {
        this.ordersDetailNum = ordersDetailNum;
        this.accomType = accomType;
        this.accomTypeIcon = accomTypeFilePath;
        this.accomName = accomName;
        this.accomRegion = accomRegion;
        this.orderDate = orderDate;
        this.usePeriod = usePeriod;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.price = price;
        this.usedStatus = usedStatus;
        this.roomName = roomName;
        this.roomNum = roomNum;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public OrdersDetailDTO(Reservation reservation) {

        Period period = Period.between(reservation.getReserveUseStartDate(), reservation.getReserveUseEndDate());
        String startDay = reservation.getReserveUseStartDate().getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.KOREAN);
        String endDay = reservation.getReserveUseEndDate().getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.KOREAN);

        this.ordersDetailNum = reservation.getReserveNum();
        this.accomType = reservation.getRoom().getAccommodation().getAccomCategory().getViewName();
        this.accomTypeIcon = reservation.getRoom().getAccommodation().getAccomCategory().getName();
        this.accomName = reservation.getRoom().getAccommodation().getAccomName();
        this.accomRegion = reservation.getRoom().getAccommodation().getRegion().getRegion().getValue();
        this.orderDate = LocalDate.now().toString();
        this.usePeriod = String.valueOf(period.getMonths() * 30 + period.getDays());
        this.startDate = reservation.getReserveUseStartDate().toString();
        this.endDate = reservation.getReserveUseEndDate().toString();
        this.startWeek = startDay;
        this.endWeek = endDay;
        this.price = String.valueOf(reservation.getReservePrice());
        this.usedStatus = reservation.getReserveStatus().toString();
        this.roomName = reservation.getRoom().getRoomName();
        this.roomNum = reservation.getRoom().getAccommodation().getAccomNum().toString();
        this.checkIn = reservation.getRoom().getAccommodation().getCheckIn().toString();
        this.checkOut = reservation.getRoom().getAccommodation().getCheckOut().toString();
        this.roomOriginFileName = reservation.getRoom().getRoomImageList().get(0).getRoomOriginFileName();
    }
}
