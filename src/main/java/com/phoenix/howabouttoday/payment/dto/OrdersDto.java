package com.phoenix.howabouttoday.payment.dto;

import com.phoenix.howabouttoday.member.entity.Member;
import com.phoenix.howabouttoday.payment.entity.Orders;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Getter

public class OrdersDto {

    private Long ordersNum;

    private Member member;

    private String ordersTel;

    private String ordersName;

    private LocalDate ordersDate;

    private Integer ordersPrice;

    private String ordersType;

    private String ordersStatus;

    private List<OrdersDetailDto> ordersDetailDtoList;

    public OrdersDto(Orders orders) {
        this.ordersNum = orders.getOrdersNum();
        this.member = orders.getMember();
        this.ordersTel = orders.getOrdersTel();
        this.ordersName = orders.getOrdersName();
        this.ordersDate = orders.getOrdersDate();
        this.ordersPrice = orders.getOrdersPrice();
        this.ordersType = orders.getOrdersType();
        this.ordersStatus = orders.getOrdersStatus();
        this.ordersDetailDtoList = orders.getReservation().stream().map(OrdersDetailDto::new).collect(Collectors.toList());
    }

    public OrdersDto() {
    }

    public OrdersDto test1(Orders orders){
        return null;
    }

}