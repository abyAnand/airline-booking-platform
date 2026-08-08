package com.anand.service.impl;

import com.anand.mapper.AirportMapper;
import com.anand.model.Airport;
import com.anand.model.City;
import com.anand.payload.request.AirportRequest;
import com.anand.payload.response.AirportResponse;
import com.anand.repository.AirportRepository;
import com.anand.repository.CityRepository;
import com.anand.service.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

    private final AirportRepository airportRepository;
    private final CityRepository cityRepository;


    @Override
    public AirportResponse createAirport(AirportRequest request) throws Exception {

        if(airportRepository.findByIataCode(request.getIataCode()).isPresent()){
            throw new Exception("Airport with IATA code Already Exist");
        }

        City city = cityRepository.findById(request.getCityId()).orElseThrow(
                () -> new Exception("City not Found"));
        Airport airport = AirportMapper.toEntity(request);
        airport.setCity(city);
        Airport savedAirport = airportRepository.save(airport);

        return AirportMapper.toResponse(savedAirport);
    }

    @Override
    public AirportResponse getAirportById(Long id) throws Exception {
        Airport airport =  airportRepository.findById(id).orElseThrow(
                () -> new Exception("Airport not Exist with provided ID"));
        return AirportMapper.toResponse(airport);
    }

    @Override
    public List<AirportResponse> getAllAirports() {
        return airportRepository.findAll().stream()
                .map(AirportMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AirportResponse updateAirport(Long id, AirportRequest request) throws Exception {
        Airport existingAirport = airportRepository.findById(id).orElseThrow(
                () -> new Exception("Airport not exist with id " + id));

        if(request.getIataCode()!= null &&
                !existingAirport.getIataCode().equals(request.getIataCode())
                && airportRepository.findByIataCode(request.getIataCode()).isPresent()
       ){
            throw new Exception("Airport with IATA Code Already Exist");
    }
        AirportMapper.updateEntity(request, existingAirport);

        Airport updatedAirport = airportRepository.save(existingAirport);
        return AirportMapper.toResponse(updatedAirport);
    }

    @Override
    public void deleteAirport(Long id) throws Exception {
        Airport airport =  airportRepository.findById(id).orElseThrow(
                () -> new Exception("Airport not Exist with provided ID"));
        airportRepository.delete(airport);
    }

    @Override
    public List<AirportResponse> getAirPortByCityId(Long cityId) {
        return airportRepository.findByCityId(cityId).stream()
                .map(AirportMapper::toResponse)
                .collect(Collectors.toList());
    }
}
