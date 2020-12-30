package com.sess.core.dao.repositories;

import com.sess.core.users.City;
import com.sess.core.users.Sex;
import com.sess.core.users.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Disabled
class UserJpaRepositoryTest {

    @Autowired
    private UserJpaRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void testSave() {
        User user = new User();
        City city = new City();
        city.setId(1L);

        user.setNickname("test");
        user.setEmail("test@mail.ru");
        user.setCity(city);
        user.setBirthday(LocalDateTime.now());
        user.setSecurityKey(UUID.randomUUID());
        user.setSex(Sex.MALE);

        User save = repository.save(user);
        assertThat(save.getId()).isNotNull();

        entityManager.flush();

        Optional<User> byId = repository.findById(save.getId());
        assertThat(byId).isNotEmpty();
        assertThat(byId.get().getNickname()).isEqualTo(user.getNickname());
    }

}