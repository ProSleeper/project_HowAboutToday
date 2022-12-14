package com.phoenix.howabouttoday.room.dto;

import com.phoenix.howabouttoday.room.entity.RoomViewService;
import com.phoenix.howabouttoday.room.entity.ServiceNames;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RoomServiceDTO {

    private Long serviceNum; //서비스 번호

    private Long roomNum; //객실 번호

    private ServiceNames serviceName; //서비스 이름

    public RoomServiceDTO(RoomViewService roomViewService) {
        this.roomNum = roomViewService.getRoom().getRoomNum();
        this.serviceName = roomViewService.getService().getServiceName();
        this.serviceNum = roomViewService.getService().getServiceNum();
    }

}
