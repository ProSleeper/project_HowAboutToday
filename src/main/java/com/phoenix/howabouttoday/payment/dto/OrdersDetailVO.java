
/**
 * 주문내역에서 보여줄 주문 vo
 *
 */

package com.phoenix.howabouttoday.payment.dto;

import com.phoenix.howabouttoday.payment.entity.OrdersDetail;
import com.phoenix.howabouttoday.reserve.domain.Reservation.Cart;
import com.phoenix.howabouttoday.reserve.domain.Reservation.Reservation;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.TextStyle;
import java.util.Locale;

@Getter
public class OrdersDetailVO {
    private Long cartNum;
    private String accomType;
    private String accomName;
    private String accomRegion;
    private String orderDate;
    private String usePeriod;
    private String startDate;
    private String endDate;
    private String startWeek;
    private String endWeek;
    private String price;
    private String roomName;
    private String checkIn;
    private String checkOut;

    private String roomNum;

    private String roomFilename;

    public OrdersDetailVO(Cart cart) {
        Period period = Period.between(cart.getReserveUseStartDate(), cart.getReserveUseEndDate());
        String startDay = cart.getReserveUseStartDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
        String endDay = cart.getReserveUseEndDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);

        this.cartNum = cart.getReserveNum();
//        this.accomType = cart.getRoom().getAccommodation().getAccomCategory().getValue();
        this.accomName = cart.getRoom().getAccommodation().getAccomName();
        this.accomRegion = cart.getRoom().getAccommodation().getRegion().getRegion().getValue();
        this.orderDate = LocalDate.now().toString();
        this.usePeriod = String.valueOf(period.getMonths() * 30 + period.getDays());
        this.startDate = cart.getReserveUseStartDate().toString();
        this.endDate = cart.getReserveUseEndDate().toString();
        this.startWeek = startDay;
        this.endWeek = endDay;
        this.price = String.valueOf(cart.getReservePrice());
        this.roomName = cart.getRoom().getRoomName();
        this.checkIn = cart.getRoom().getAccommodation().getCheckIn().toString();
        this.checkOut = cart.getRoom().getAccommodation().getCheckOut().toString();
        this.roomNum = cart.getRoom().getRoomNum().toString();
        this.roomFilename = cart.getRoom().getRoomImageList().get(0).getRoomOriginFileName();
    }

    public OrdersDetailVO(OrdersDetail ordersDetail) {

        Period period = Period.between(ordersDetail.getReserveUseStartDate(), ordersDetail.getReserveUseEndDate());
        String startDay = ordersDetail.getReserveUseStartDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
        String endDay = ordersDetail.getReserveUseEndDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);

        this.cartNum = ordersDetail.getReserveNum();
//        this.accomType = ordersDetail.getRoom().getAccommodation().getAccomCategory().getValue();
        this.accomName = ordersDetail.getRoom().getAccommodation().getAccomName();
        this.accomRegion = ordersDetail.getRoom().getAccommodation().getRegion().getRegion().getValue();
        this.orderDate = LocalDate.now().toString();
        this.usePeriod = String.valueOf(period.getMonths() * 30 + period.getDays());
        this.startDate = ordersDetail.getReserveUseStartDate().toString();
        this.endDate = ordersDetail.getReserveUseEndDate().toString();
        this.startWeek = startDay;
        this.endWeek = endDay;
        this.price = String.valueOf(ordersDetail.getReservePrice());
        this.roomName = ordersDetail.getRoom().getRoomName();
        this.checkIn = ordersDetail.getRoom().getAccommodation().getCheckIn().toString();
        this.checkOut = ordersDetail.getRoom().getAccommodation().getCheckOut().toString();
    }


    //즉시 결제시 사용하는 날짜 * 금액을 반환하는 메서드
    public Integer totalPrice(){
        return Integer.parseInt(getPrice());
    }

}
