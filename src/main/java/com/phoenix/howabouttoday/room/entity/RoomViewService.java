package com.phoenix.howabouttoday.room.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomViewService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomViewAmenitiesNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_num")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_num")
    private Service service;

}
