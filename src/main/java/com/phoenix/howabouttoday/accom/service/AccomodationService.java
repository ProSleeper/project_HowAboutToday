package com.phoenix.howabouttoday.accom.service;


import com.phoenix.howabouttoday.accom.dto.AccomDto;
import com.phoenix.howabouttoday.accom.dto.AccommodationDTO;
import com.phoenix.howabouttoday.accom.dto.SearchForm;
import com.phoenix.howabouttoday.accom.entity.AccomImage;
import com.phoenix.howabouttoday.accom.entity.Accommodation;
import com.phoenix.howabouttoday.accom.entity.Region;
import com.phoenix.howabouttoday.accom.repository.AccommodationImageRepository;
import com.phoenix.howabouttoday.accom.repository.AccommodationRepository;
import com.phoenix.howabouttoday.accom.repository.RegionRepository;
import com.phoenix.howabouttoday.room.entity.AvailableDate;
import com.phoenix.howabouttoday.room.entity.Room;
import lombok.Builder;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AccomodationService {
    private final AccommodationRepository accommodationRepository;

    private final RegionRepository regionRepository;
    private final AccommodationImageRepository accommodationImageRepository;

    /** 전체숙소 lowpriceTest **/
    public List<Accommodation> getAccoms(){
        List<Accommodation> all = accommodationRepository.findAll();
        for (Accommodation accommodation : all) {
            accommodation.modifyLowPrice(accommodation);
        }
        return all;
    }

    /** 지역이 없을때 전체조회 **/
    public Slice<AccomDto.ResponsePageDto> getAccomPageList(Pageable pageable,String category_name , String keyword,int maxPrice,int minPrice, Double accomRating) {

        Slice<Accommodation> page =
                accommodationRepository.
                       findByAccomCategory_NameAndLowPriceLessThanEqualAndLowPriceGreaterThanEqualAndAccomRatingLessThanEqualAndAccomNameContaining(category_name,
                        maxPrice, minPrice, pageable,accomRating, keyword);




        Slice<AccomDto.ResponsePageDto> accomPageList = page.map(accom -> new AccomDto.ResponsePageDto(accom));

        return accomPageList;
    }

    /** 지역에 대한 전체조회 **/
    public Slice<AccomDto.ResponsePageDto> getByRegionAccomPageList(Long regionNum,Pageable pageable,String category_name , String keyword,int maxPrice,int minPrice,Double accomRating) {

        Slice<Accommodation> page =
                accommodationRepository.
                        findByRegion_RegionNumAndAccomCategory_NameAndLowPriceLessThanEqualAndLowPriceGreaterThanEqualAndAccomRatingLessThanEqualAndAccomNameContaining
                                (regionNum,category_name,maxPrice,minPrice,pageable, accomRating,keyword);

        Slice<AccomDto.ResponsePageDto> accomPageList = page.map(accom -> new AccomDto.ResponsePageDto(accom));

        return accomPageList;
    }

    /** accomNum에 해당하는 숙소 조회 **/
    public AccommodationDTO findByAccomNum(Long accomNum, SearchForm searchForm){

        Accommodation findAccom = accommodationRepository.findById(accomNum).orElseThrow(() ->
                new IllegalArgumentException("해당 숙소는 현재 존재하지 않습니다"));

        String reserveDate = searchForm.getDaterange();
        /** 멀티데이트를 스플릿해서 reserveForm에 넘겨주기위한 준비작업 **/
        String[] splitDate = searchForm.getDaterange().split("-");

        //날짜 패턴에 공백이 있어서 양쪽 공백제거 작업
        String date1 = splitDate[0].strip();
        String date2 = splitDate[1].strip();

        LocalDate startDate = StringToParseDate(date1);
        LocalDate endDate = StringToParseDate(date2);

        List<Room> findAccomRoom = findAccom.getRoom();
        List<Room> removed = new ArrayList<>();
        for (Room room : findAccomRoom) {
            //희망예약 인원이 객실의 최대인원보다 크다면 해당객실은 제거
            if( room.getMaxGuest().intValue() < (searchForm.getAdult_number() + searchForm.getChild_number())){
                removed.add(room);
            }
            List<AvailableDate> availableDate = room.getAvailableDate();

            for (AvailableDate date : availableDate) {
                //희망예약 날짜가 이미 예약이 된 날짜라면 해당객실 제거
                if(date.getOneDay().isEqual(startDate) || date.getOneDay().isEqual(endDate)){
                    removed.add(room);
                }
            }
        }
        findAccom.getRoom().removeAll(removed);
        AccommodationDTO accomDto = new AccommodationDTO(findAccom);
        return accomDto;

    }




    public AccommodationDTO convertEntityToDto(Accommodation accommodation) {

        return AccommodationDTO.builder()
                .accomName(accommodation.getAccomName())
                .accomTel(accommodation.getAccomTel())
//                .accomAddress(accommodation.getAccomAddress())
                //.accomRating(accommodation.getAccomRating())
                .accomWishlistCount(accommodation.getAccomWishlistCount())
//                .totalreviewNum(accommodation.getTotalReviewNum())
                .latitude(accommodation.getLatitude())
                .longitude(accommodation.getLongitude())
                .lowPrice(accommodation.getLowPrice().toString())
                .reserveRange(accommodation.getReserveRange())
                .build();

    }

    @Transactional
    public Accommodation findAccom(Long accomNum) {

        Optional<Accommodation> findAccom = accommodationRepository.findById(accomNum);
        Accommodation accommodation = findAccom.orElseThrow(() ->
                new IllegalArgumentException("해당 숙소가 존재하지 않습니다"));

        return accommodation;
    }


    /** 부모 regionNum에 따른 전체리스트 조회 **/
    public Slice<AccomDto.ResponsePageDto> findByRegionNum(Long regionNum, Pageable pageable){

        Slice<Accommodation> findAccoms = accommodationRepository.findByRegion_ParentRegion_RegionNum(regionNum, pageable);

        return findAccoms.map(accom -> new AccomDto.ResponsePageDto(accom));


    }

    /** 인기숙소 조회 **/
    public Slice<AccommodationDTO> findByPplAccoms(Pageable pageable){

        // 숙소평점이 3.0보다 높은 숙소들을 조회해온다
        Slice<Accommodation> findAccoms = accommodationRepository.findByAccomRatingGreaterThan(3.0, pageable);

        return findAccoms.map(accom -> new AccommodationDTO(accom));
    }

    /** 스트링타입을 LocalDate타입으로 파싱해주는 메서드 **/
    public LocalDate StringToParseDate(String date){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate parseDate = LocalDate.parse(date, formatter);
        return parseDate;
    }

}

