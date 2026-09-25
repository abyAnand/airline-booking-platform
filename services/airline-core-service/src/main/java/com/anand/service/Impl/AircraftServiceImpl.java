package com.anand.service.Impl;

import com.anand.mapper.AircraftMapper;
import com.anand.model.Aircraft;
import com.anand.model.Airline;
import com.anand.payload.request.AircraftRequest;
import com.anand.payload.response.AircraftResponse;
import com.anand.repository.AircraftRepository;
import com.anand.repository.AirlineRepository;
import com.anand.service.AircraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AircraftServiceImpl implements AircraftService {

    private final AirlineRepository airlineRepository;
    private final AircraftRepository aircraftRepository;


    @Override
    public AircraftResponse getAircraftById(Long id) throws Exception{
        Aircraft aircraft =  aircraftRepository.findById(id).orElseThrow(
                () -> new Exception("Aircraft not exist with the Id")
        );
        return AircraftMapper.toResponse(aircraftRepository.save(aircraft));
    }

    @Override
    public List<AircraftResponse> listAllAircraftsByOwner(Long ownerId) throws Exception {
        Airline airline = airlineRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new Exception("Airline not found for owner: " + ownerId));
        return aircraftRepository.findByAirlineId(airline.getId())
                .stream()
                .map(AircraftMapper::toResponse)
                .toList();
    }

    @Override
    public AircraftResponse createAircraft(AircraftRequest request, Long ownerId)
            throws Exception {
        Airline airline = airlineRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new Exception("Airline not found for owner: " + ownerId));

        Aircraft aircraft = AircraftMapper.toEntity(request,airline);
        if(aircraftRepository.existByCode(aircraft.getCode())){
            throw new Exception("code already exist with another aircraft");
        }
        if(aircraft.getSeatingCapacity() < aircraft.getTotalSeats()){
            throw new Exception("seating capacity can't exceed totalSeats");
        }
        return AircraftMapper.toResponse(
                aircraftRepository.save(aircraft)
        );

    }

    @Override
    public AircraftResponse updateAircraft(Long id, AircraftRequest request, Long ownerId)
            throws Exception {
        Airline airline = airlineRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new Exception("Airline not found for owner: " + ownerId));

        Aircraft aircraft = aircraftRepository.findByIdAndAirlineId(id, airline.getId());
        if(aircraft == null){
            throw new Exception("Aircraft not exist with id: " + id);
        }
        if(request.getCode()!= null &&
                !aircraft.getCode().equals(request.getCode()) &&
                aircraftRepository.existByCode(request.getCode())){
            throw new Exception("code already exist with another aircraft");
        }

        AircraftMapper.updateEntity(aircraft, request,airline);
        return AircraftMapper.toResponse(
                aircraftRepository.save(aircraft)
        );
    }

    @Override
    public void deleteAircraft(Long id, Long ownerId) throws Exception {
        Airline airline = airlineRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new Exception("Airline not found for owner: " + ownerId));
        Aircraft aircraft = aircraftRepository.findByIdAndAirlineId(id, airline.getId());
        if(aircraft == null){
            throw new Exception("Aircraft not exist with id: " + id);
        }
        aircraftRepository.delete(aircraft);
    }

}
