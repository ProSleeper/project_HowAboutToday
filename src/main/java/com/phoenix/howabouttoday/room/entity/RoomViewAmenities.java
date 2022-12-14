package com.phoenix.howabouttoday.room.entity;

import lombok.*;

import javax.persistence.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
public class RoomViewAmenities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long roomViewAmenitiesNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_num")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amenities_num")
    private Amenities amenities;


    @Builder
    public RoomViewAmenities(Room room, Amenities amenities) {
        this.amenities = amenities;
        this.room = room;

    }

}
