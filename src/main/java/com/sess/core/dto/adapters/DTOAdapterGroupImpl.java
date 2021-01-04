package com.sess.core.dto.adapters;

import com.sess.core.dto.DTOCity;
import com.sess.core.dto.DTOUser;
import com.sess.core.dto.GroupDTO;
import com.sess.core.dto.adapters.exceptions.ConvertFromDTOException;
import com.sess.core.dto.adapters.exceptions.ConvertToDTOException;
import com.sess.core.groups.Group;
import com.sess.core.cities.City;
import com.sess.core.users.User;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DTOAdapterGroupImpl implements DTOAdapterGroup {

    private final CityDTOAdapter cityAdapter;

    private final UserDTOAdapter userAdapter;

    public DTOAdapterGroupImpl(CityDTOAdapter cityAdapter, UserDTOAdapter userAdapter) {
        this.cityAdapter = cityAdapter;
        this.userAdapter = userAdapter;
    }

    @Override
    public Group convertFromDTO(GroupDTO groupDTO) throws ConvertFromDTOException {
        if (Objects.isNull(groupDTO)) {
            return null;
        }

        City city = cityAdapter.convertFromDTO(groupDTO.getCity());
        User user = userAdapter.convertFromDTO(groupDTO.getCreator());

        Group group = new Group();
        group.setId(group.getId());
        group.setTitle(group.getTitle());
        group.setDescription(group.getDescription());
        group.setCreator(user);
        group.setCity(city);
        return group;
    }

    @Override
    public GroupDTO convertToDTO(Group group) throws ConvertToDTOException {
        if (Objects.isNull(group)) {
            return null;
        }

        DTOCity city = cityAdapter.convertToDTO(group.getCity());
        DTOUser creator = userAdapter.convertToDTO(group.getCreator());
        return new GroupDTO(
                group.getId(),
                city,
                creator,
                group.getTitle(),
                group.getDescription()
        );
    }
}
