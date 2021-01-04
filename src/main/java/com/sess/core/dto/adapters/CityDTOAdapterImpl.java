package com.sess.core.dto.adapters;

import com.sess.core.dto.DTOCity;
import com.sess.core.dto.adapters.exceptions.ConvertFromDTOException;
import com.sess.core.dto.adapters.exceptions.ConvertToDTOException;
import com.sess.core.cities.City;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CityDTOAdapterImpl implements CityDTOAdapter {

    @Override
    public City convertFromDTO(DTOCity DTOCity) throws ConvertFromDTOException {
        if (Objects.isNull(DTOCity)) {
            return null;
        }

        City city = new City();
        city.setId(DTOCity.getId());
        city.setAddress(DTOCity.getAddress());
        return city;
    }

    @Override
    public DTOCity convertToDTO(City city) throws ConvertToDTOException {
        if (Objects.isNull(city)) {
            return null;
        }

        return new DTOCity(
                city.getId(),
                city.getAddress()
        );
    }
}
