package com.sess.core.dto.adapters;

import com.sess.core.dto.DTOCity;
import com.sess.core.dto.DTOUser;
import com.sess.core.dto.adapters.exceptions.ConvertFromDTOException;
import com.sess.core.dto.adapters.exceptions.ConvertToDTOException;
import com.sess.core.cities.City;
import com.sess.core.users.User;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserDTOAdapterImpl implements UserDTOAdapter {

    private final CityDTOAdapter cityAdapter;

    public UserDTOAdapterImpl(CityDTOAdapter cityAdapter) {
        this.cityAdapter = cityAdapter;
    }

    @Override
    public User convertFromDTO(DTOUser dto) throws ConvertFromDTOException {
        if (Objects.isNull(dto)) {
            return null;
        }

        City city = cityAdapter.convertFromDTO(dto.getCity());
        User entity = new User();
        entity.setId(dto.getId());
        entity.setNickname(dto.getNickname());
        entity.setEmail(dto.getEmail());
        entity.setSex(dto.getSex());
        entity.setBirthday(dto.getBirthday());
        entity.setSecurityKey(dto.getSecurityKey());
        entity.setCity(city);
        return entity;
    }

    @Override
    public DTOUser convertToDTO(User user) throws ConvertToDTOException {
        if (Objects.isNull(user)) {
            return null;
        }

        DTOCity DTOCity = cityAdapter.convertToDTO(user.getCity());
        return new DTOUser(
                user.getId(),
                user.getNickname(),
                user.getEmail(),
                DTOCity,
                user.getSex(),
                user.getBirthday(),
                user.getSecurityKey()
        );
    }
}
