package com.anand.service.Impl;

import com.anand.enums.AirlineStatus;
import com.anand.mapper.AirlineMapper;
import com.anand.model.Airline;
import com.anand.payload.request.AirlineRequest;
import com.anand.payload.response.AirlineDropdownItem;
import com.anand.payload.response.AirlineResponse;
import com.anand.repository.AirlineRepository;
import com.anand.service.AirlineService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirlineServiceImpl implements AirlineService {

    private final AirlineRepository airlineRepository;

    @Override
    public AirlineResponse createAirline(AirlineRequest request, Long ownerId) {
        Airline airline = AirlineMapper.toEntity(request, ownerId);
        Airline saved = airlineRepository.save(airline);
        return AirlineMapper.toResponse(saved);
    }

    @Override
    public AirlineResponse getAirlineByOwner(Long ownerId) {
        Airline airline = airlineRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Airline not found for owner: " + ownerId));
        return AirlineMapper.toResponse(airline);
    }

    @Override
    public AirlineResponse getAirlineById(Long id) {
        Airline airline = airlineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Airline not found"));
        return AirlineMapper.toResponse(airline);
    }

    @Override
    public Page<AirlineResponse> getAllAirlines(Pageable pageable) {
        return airlineRepository
                .findAll(pageable).map(AirlineMapper::toResponse);
    }

    @Override
    public AirlineResponse updateAirline(AirlineRequest request, Long ownerId) {
        Airline airline = airlineRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Airline not found for owner: " + ownerId));

        AirlineMapper.updateEntity(airline, request);
        return AirlineMapper.toResponse(airlineRepository.save(airline));
    }

    @Override
    public void deleteAirline(Long id, Long ownerId) {
        Airline airline = airlineRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Airline not found for owner: " + ownerId));
        airlineRepository.delete(airline);
    }

    @Override
    public AirlineResponse changeStatusByAdmin(Long airlineId, AirlineStatus status) {
        Airline airline = airlineRepository.findById(airlineId)
                .orElseThrow(() -> new EntityNotFoundException("Airline not found with ID: " + airlineId));
        airline.setStatus(status);
        return AirlineMapper.toResponse(airlineRepository.save(airline));
    }

    @Override
    public List<AirlineDropdownItem> getAirlinesForDropdown() {
        return airlineRepository.findByStatus(AirlineStatus.ACTIVE).stream()
                .map(a -> AirlineDropdownItem.builder()
                        .id(a.getId())
                        .name(a.getName())
                        .iataCode(a.getIataCode())
                        .icaoCode(a.getIcaoCode())
                        .logoUrl(a.getLogoUrl())
//                        .country(a.getCountry())
                        .build())
                .toList();
    }
}
