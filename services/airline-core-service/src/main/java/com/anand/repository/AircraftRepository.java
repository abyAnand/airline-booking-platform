package com.anand.repository;

import com.anand.enums.AircraftStatus;
import com.anand.model.Aircraft;
import com.anand.model.Airline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AircraftRepository extends JpaRepository<Aircraft, Long> {

   List<Aircraft> findByAirlineId(Long airlineId);
   boolean existByCode(String code);
   Aircraft findByIdAndAirlineId(Long id, Long airlineId);

}


