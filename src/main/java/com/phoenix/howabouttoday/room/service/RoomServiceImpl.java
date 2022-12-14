package com.phoenix.howabouttoday.room.service;
import com.phoenix.howabouttoday.room.dto.*;
import com.phoenix.howabouttoday.room.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
@Repository
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomViewAmenitiesRepository roomViewAmenitiesRepository;
    private final RoomViewServiceRepository roomViewServiceRepository;
    private final RoomImageRepository roomImageRepository;

    private final RoomReviewRepository roomReviewRepository;

    //객실 리스트
    @Override
    public List<RoomListDTO> findAll_Room(Long accomNum) {
        List<RoomListDTO> lists = roomRepository.findAllByAccommodationAccomNum(accomNum)
                .stream()
                .map(RoomListDTO::new)
                .collect(Collectors.toList());
        return lists;
    }

    // 시설 리스트
    @Override
    public List<RoomAmenitiesDTO> findAll_Amenities(Long roomNum) {
        List<RoomAmenitiesDTO> alist = roomViewAmenitiesRepository.findAllByRoom_RoomNum(roomNum)
                .stream()
                .map(RoomAmenitiesDTO::new)
                .collect(Collectors.toList());
        return alist;

    }

    //서비스 리스트
    @Override
    public List<RoomServiceDTO> findAll_Service(Long roomNum) {
        List<RoomServiceDTO> slist = roomViewServiceRepository.findAllByRoom_RoomNum(roomNum)
                .stream()
                .map(RoomServiceDTO::new)
                .collect(Collectors.toList());
        return slist;
    }

    // 객실 이미지
    public List<RoomImageDTO> findAll_Image(Long roomNum) {
        List<RoomImageDTO> ilist = roomImageRepository.findAllByRoom_RoomNum(roomNum)
                .stream()
                .map(RoomImageDTO::new)
                .collect(Collectors.toList());
        return ilist;
    }

    //객실 디테일
    @Override
    public RoomDetailDTO findOne_Room(Long roomNum) {
        return roomRepository.findByRoomNum(roomNum)
                .map(RoomDetailDTO::new)
                .orElse(null);

    }

    @Override
    public List<RoomReviewDTO> findAll_Review(Long roomNum) {
        List<RoomReviewDTO> rlist = roomReviewRepository.findAllByRoom_RoomNum(roomNum)
                .stream()
                .map(RoomReviewDTO::new)
                .collect(Collectors.toList());

        return rlist;
    }

}
