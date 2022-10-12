package com.phoenix.howabouttoday.payment.repository;


import com.phoenix.howabouttoday.accom.entity.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationRepository_ogirin extends JpaRepository<Accommodation, Long> {

    @Override
    <S extends Accommodation> S save(S entity);

    Accommodation findByAccomNum(Long number);

    @Override
    List<Accommodation> findAll();
}