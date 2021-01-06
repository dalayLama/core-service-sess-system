package com.sess.core.api.rest.handlers;

import com.sess.core.cities.CityService;
import com.sess.core.dto.DTOCity;
import com.sess.core.dto.adapters.CityDTOAdapter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityRestApiHandlerService implements CityRestApiHandler {

    private final CityService cityService;

    private final CityDTOAdapter cityAdapter;

    public CityRestApiHandlerService(CityService cityService, CityDTOAdapter cityAdapter) {
        this.cityService = cityService;
        this.cityAdapter = cityAdapter;
    }

    @Override
    public List<DTOCity> getAllCities() {
        return cityService.getAllCities().stream()
                .map(cityAdapter::convertToDTO)
                .collect(Collectors.toList());
    }
}
