package com.sess.core.dao.repositories;

import com.sess.core.TestUtils;
import com.sess.core.groups.Group;
import com.sess.core.roles.Role;
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
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Disabled
class UserJpaRepositoryTest {

    @Autowired
    private UserJpaRepository userRepository;

    @Autowired
    private JpaGroupRepository groupRepository;

    @Autowired
    private RoleJpaRepository roleRepository;

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

        User save = userRepository.save(user);
        assertThat(save.getId()).isNotNull();

        entityManager.flush();

        Optional<User> byId = userRepository.findById(save.getId());
        assertThat(byId).isNotEmpty();
        assertThat(byId.get().getNickname()).isEqualTo(user.getNickname());
    }

    @Test
    public void testSave2() {
        User creator = userRepository.save(TestUtils.createNewUser());
        User user = userRepository.save(TestUtils.createNewUser());
        entityManager.flush();

        Group group = TestUtils.createNewGroup(creator);
        groupRepository.save(group);
        group.addUser(user);
        user.addGroup(group);
        entityManager.flush();
        entityManager.detach(user);

        Role role = roleRepository.findById(1L).get();
        user = userRepository.findById(user.getId()).get();
        user.addRole(group.getId(), role);
        userRepository.save(user);
        entityManager.flush();
        entityManager.detach(user);

        user = userRepository.findById(user.getId()).get();
        List<Role> roles = new ArrayList<>();
        user.getUserOfGroups().forEach(ug -> roles.addAll(ug.getRoles()));

        assertThat(roles.stream().map(Role::getId).collect(Collectors.toList()))
                .containsExactlyInAnyOrderElementsOf(Collections.singleton(role.getId()));

        user.removeRole(group.getId(), role.getId());
        userRepository.save(user);
        entityManager.flush();
        user = userRepository.findById(user.getId()).get();
        assertThat(user.getRoles(group.getId())).isEmpty();
    }

}