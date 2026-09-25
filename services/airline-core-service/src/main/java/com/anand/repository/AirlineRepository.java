package com.anand.repository;

import com.anand.enums.AirlineStatus;
import com.anand.model.Airline;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface AirlineRepository extends JpaRepository<Airline, Long> {

    Optional<Airline> findByOwnerId(Long ownerId);

    Optional<Airline> findByIataCode(String code);

    Optional<Airline> findByIcaoCode(String code);

    boolean existsByIataCode(String code);

    boolean existsByIcaoCode(String code);

    Page<Airline> findByStatus(AirlineStatus status, Pageable pageable);

    List<Airline> findByStatus(AirlineStatus status);
}