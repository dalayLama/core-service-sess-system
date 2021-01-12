package com.sess.core.dao.repositories;

import com.sess.core.TestUtils;
import com.sess.core.groups.Group;
import com.sess.core.running.RunningType;
import com.sess.core.users.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
@Disabled
class RunningTypeJpaRepositoryTest {

    @Autowired
    private RunningTypeJpaRepository runningRepository;

    @Autowired
    private JpaGroupRepository groupRepository;

    @Autowired
    private UserJpaRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void testSaveAndGet() {
        User newUser = TestUtils.createNewUser();
        User user = userRepository.saveAndFlush(newUser);
        Group newGroup = TestUtils.createNewGroup(user);
        groupRepository.saveAndFlush(newGroup);

        RunningType newRunningType = TestUtils.createNewRunningType(newGroup);
        RunningType runningType = runningRepository.saveAndFlush(newRunningType);
        entityManager.detach(runningType);

        boolean exists = runningRepository.exists(newGroup.getId(), newRunningType.getCaption());
        boolean notExist = runningRepository.exists(newGroup.getId(), newRunningType.getCaption(), newRunningType.getId());
        assertThat(exists).isTrue();
        assertThat(notExist).isFalse();
    }

}