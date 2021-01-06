package com.sess.core.dao.repositories;

import com.sess.core.TestUtils;
import com.sess.core.groups.Group;
import com.sess.core.users.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Disabled
class JpaGroupRepositoryTest {

    @Autowired
    private JpaGroupRepository groupRepository;

    @Autowired
    private UserJpaRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void testSave() {
        int countUsers = 3;
        List<User> users = new ArrayList<>();
        for (int i = 0; i < countUsers; i++) {
            users.add(TestUtils.createNewUser());
        }
        User creator = TestUtils.createNewUser();
        List<User> savedUsers = userRepository.saveAll(users);
        User savedCreator = userRepository.save(creator);
        entityManager.flush();

        Group newGroup = TestUtils.createNewGroup(savedCreator);
        newGroup.getUsers().addAll(savedUsers);
        savedUsers.forEach(u -> u.getGroups().add(newGroup));
        Group savedGroup = groupRepository.save(newGroup);

        entityManager.flush();
        entityManager.detach(savedGroup);

        Optional<Group> byId = groupRepository.findById(savedGroup.getId());
        assertThat(byId.isPresent()).isTrue();
        assertThat(savedUsers.stream().map(User::getId).collect(Collectors.toList()))
                .containsExactlyInAnyOrderElementsOf(
                        byId.get().getUsers().stream().map(User::getId).collect(Collectors.toList())
                );
        for (User user : byId.get().getUsers()) {
            assertThat(user.getGroups().stream().map(Group::getId).collect(Collectors.toList()))
                    .containsExactlyInAnyOrderElementsOf(Collections.singleton(byId.get().getId()));
        }
    }

    @Test
    public void testRemoveUsersFromGroup() {
        int countUsers = 3;
        List<User> users = new ArrayList<>();
        for (int i = 0; i < countUsers; i++) {
            users.add(TestUtils.createNewUser());
        }
        User creator = TestUtils.createNewUser();
        List<User> savedUsers = userRepository.saveAll(users);
        User savedCreator = userRepository.save(creator);
        entityManager.flush();

        Group mainGroup = createGroupForUsers(savedCreator, savedUsers);
        Group additionalGroup = createGroupForUsers(savedCreator, savedUsers);
        Group savedMainGroup = groupRepository.save(mainGroup);
        Group savedAdditionalGroup = groupRepository.save(additionalGroup);

        entityManager.flush();
        entityManager.detach(savedMainGroup);
        entityManager.detach(savedAdditionalGroup);

        Group dbAdditionalGroup = groupRepository.findById(savedAdditionalGroup.getId()).get();
        dbAdditionalGroup.getUsers().forEach(u -> u.removeGroup(dbAdditionalGroup.getId()));
        dbAdditionalGroup.getUsers().clear();
        Group savedAdditionalGroup2 = groupRepository.save(dbAdditionalGroup);
        entityManager.flush();
        entityManager.detach(dbAdditionalGroup);
        entityManager.detach(savedAdditionalGroup2);

        Optional<Group> byId = groupRepository.findById(dbAdditionalGroup.getId());
        List<User> allById = userRepository.findAllById(savedUsers.stream().map(User::getId).collect(Collectors.toList()));
        assertThat(byId.isPresent()).isTrue();
        assertThat(byId.get().getUsers()).isEmpty();
        for (User user : allById) {
            assertThat(user.getGroups().stream().map(Group::getId).collect(Collectors.toList()))
                    .containsExactlyInAnyOrderElementsOf(Collections.singleton(mainGroup.getId()));
        }
    }

    private Group createGroupForUsers(User creator, Collection<User> users) {
        Group newGroup = TestUtils.createNewGroup(creator);
        newGroup.getUsers().addAll(users);
        users.forEach(u -> u.getGroups().add(newGroup));
        return newGroup;
    }

}