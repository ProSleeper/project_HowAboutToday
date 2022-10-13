package com.phoenix.howabouttoday.room.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Service {

    @Id
    @GeneratedValue
    @Column
    private Integer serviceNum;

    @Column
    private String serviceName;

}