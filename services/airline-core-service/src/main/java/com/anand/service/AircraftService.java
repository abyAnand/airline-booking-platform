package com.anand.service;

import com.anand.payload.request.AircraftRequest;
import com.anand.payload.response.AircraftResponse;

import java.util.List;

public interface AircraftService {

    AircraftResponse getAircraftById(Long id) throws Exception;

    List<AircraftResponse> listAllAircraftsByOwner(Long ownerId) throws Exception;

    AircraftResponse createAircraft(AircraftRequest request,
                                    Long ownerId) throws Exception;

    AircraftResponse updateAircraft(Long id, AircraftRequest request, Long ownerId) throws Exception;

    void deleteAircraft(Long id, Long ownerId) throws Exception ;
}
