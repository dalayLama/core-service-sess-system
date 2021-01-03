package com.sess.core;

import com.sess.core.dto.DTOCity;
import com.sess.core.dto.DTOUser;
import com.sess.core.users.City;
import com.sess.core.users.Sex;
import com.sess.core.users.User;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class TestUtils {

    private static long ID_USER = 1;

    private TestUtils() {}

    public static User createUser() {
        long newId = newId();
        User user = new User();
        user.setId(newId);
        user.setNickname(String.format("nick%d", newId));
        user.setEmail(String.format("email%d@mail.ru", newId));
        user.setSex(Sex.MALE);
        user.setCity(createCity());
        user.setSecurityKey(UUID.randomUUID());
        user.setBirthday(LocalDateTime.now());
        return user;
    }

    public static DTOUser createDTOUser(Long id) {
        return new DTOUser(
                id,
                String.format("nick%d", id),
                String.format("email%d@mail.ru", id),
                createDTOCity(),
                Sex.MALE,
                LocalDateTime.now(),
                UUID.randomUUID()
        );
    }

    public static DTOCity createDTOCity() {
        return new DTOCity(
                1L,
                "address"
        );
    }

    public static City createCity() {
        City city = new City();
        city.setId(1L);
        city.setAddress("address");
        return city;
    }

    private static long newId() {
        return ID_USER++;
    }

}
