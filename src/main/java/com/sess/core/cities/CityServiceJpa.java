package com.sess.core.cities;

import com.sess.core.dao.repositories.CityJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceJpa implements CityService {

    private final CityJpaRepository cityRepository;

    public CityServiceJpa(CityJpaRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }
}
