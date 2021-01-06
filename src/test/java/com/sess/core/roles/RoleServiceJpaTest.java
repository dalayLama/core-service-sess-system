package com.sess.core.roles;

import com.sess.core.TestUtils;
import com.sess.core.components.message.MessageId;
import com.sess.core.components.message.MessageService;
import com.sess.core.dao.repositories.JpaGroupRepository;
import com.sess.core.dao.repositories.RoleJpaRepository;
import com.sess.core.dao.repositories.UserJpaRepository;
import com.sess.core.exceptions.DeleteException;
import com.sess.core.exceptions.ErrorMessage;
import com.sess.core.exceptions.SaveException;
import com.sess.core.groups.Group;
import com.sess.core.groups.exceptions.GroupNotFoundException;
import com.sess.core.users.User;
import com.sess.core.users.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RoleServiceJpaTest {

    @Test
    public void shouldAddRole() {
        RoleJpaRepository roleRepository = mock(RoleJpaRepository.class);
        UserJpaRepository userRepository = mock(UserJpaRepository.class);
        JpaGroupRepository groupRepository = mock(JpaGroupRepository.class);
        MessageService messageService = mock(MessageService.class);

        User creator = TestUtils.createUser();
        User user = TestUtils.createUser();
        Role role = TestUtils.createRole();
        Group group = TestUtils.createGroupWithUsers(creator, Collections.singleton(user));

        when(groupRepository.findById(group.getId()))
                .thenReturn(Optional.of(group));

        RoleServiceJpa roleService = new RoleServiceJpa(roleRepository, userRepository, groupRepository, messageService);
        roleService.addRoles(group.getId(), user.getId(), role);

        verify(userRepository).save(user);
        assertThat(user.getRoles(group.getId())).contains(role);
    }

    @Test
    public void shouldRemoveRole() {
        RoleJpaRepository roleRepository = mock(RoleJpaRepository.class);
        UserJpaRepository userRepository = mock(UserJpaRepository.class);
        JpaGroupRepository groupRepository = mock(JpaGroupRepository.class);
        MessageService messageService = mock(MessageService.class);

        User creator = TestUtils.createUser();
        User user = TestUtils.createUser();
        Role role = TestUtils.createRole();
        Group group = TestUtils.createGroupWithUsers(creator, Collections.singleton(user));
        user.addRole(group.getId(), role);

        when(groupRepository.findById(group.getId()))
                .thenReturn(Optional.of(group));

        RoleServiceJpa roleService = new RoleServiceJpa(roleRepository, userRepository, groupRepository, messageService);
        roleService.removeRoles(group.getId(), user.getId(), role);

        verify(userRepository).save(user);
        assertThat(user.getRoles(group.getId())).isEmpty();
    }

    @Test
    public void shouldThrownGroupNotFoundExceptionWhenAddingRole() {
        RoleJpaRepository roleRepository = mock(RoleJpaRepository.class);
        UserJpaRepository userRepository = mock(UserJpaRepository.class);
        JpaGroupRepository groupRepository = mock(JpaGroupRepository.class);
        MessageService messageService = mock(MessageService.class);

        User creator = TestUtils.createUser();
        User user = TestUtils.createUser();
        Role role = TestUtils.createRole();
        Group group = TestUtils.createGroupWithUsers(creator, Collections.singleton(user));
        List<ErrorMessage> expectedMessages = List.of(
                new ErrorMessage("1", "message 1")
        );

        when(groupRepository.findById(group.getId()))
                .thenReturn(Optional.empty());
        when(messageService.sayError(MessageId.GROUP_NOT_FOUND, group.getId()))
                .thenReturn(expectedMessages.get(0));

        RoleServiceJpa roleService = new RoleServiceJpa(roleRepository, userRepository, groupRepository, messageService);

        SaveException thrown = assertThrows(
                SaveException.class,
                () -> roleService.addRoles(group.getId(), user.getId(), role)
        );

        verify(userRepository, never()).save(user);
        assertThat(thrown.getError().isResultOperation()).isFalse();
        assertThat(thrown.getCause()).isExactlyInstanceOf(GroupNotFoundException.class);
        assertThat(new ArrayList<ErrorMessage>(thrown.getError().getMessages()))
                .containsExactlyInAnyOrderElementsOf(expectedMessages);
        assertThat(user.getRoles(group.getId())).isEmpty();
    }

    @Test
    public void shouldThrownUserNotFoundExceptionWhenAddingRole() {
        RoleJpaRepository roleRepository = mock(RoleJpaRepository.class);
        UserJpaRepository userRepository = mock(UserJpaRepository.class);
        JpaGroupRepository groupRepository = mock(JpaGroupRepository.class);
        MessageService messageService = mock(MessageService.class);

        User creator = TestUtils.createUser();
        User user = TestUtils.createUser();
        Role role = TestUtils.createRole();
        Group group = TestUtils.createGroup(creator);
        List<ErrorMessage> expectedMessages = List.of(
                new ErrorMessage("1", "message 1")
        );

        when(groupRepository.findById(group.getId()))
                .thenReturn(Optional.of(group));
        when(messageService.sayError(MessageId.USER_NOT_FOUND_IN_GROUP, user.getId(), group.getId()))
                .thenReturn(expectedMessages.get(0));

        RoleServiceJpa roleService = new RoleServiceJpa(roleRepository, userRepository, groupRepository, messageService);

        SaveException thrown = assertThrows(
                SaveException.class,
                () -> roleService.addRoles(group.getId(), user.getId(), role)
        );

        verify(userRepository, never()).save(user);
        assertThat(thrown.getError().isResultOperation()).isFalse();
        assertThat(thrown.getCause()).isExactlyInstanceOf(UserNotFoundException.class);
        assertThat(new ArrayList<ErrorMessage>(thrown.getError().getMessages()))
                .containsExactlyInAnyOrderElementsOf(expectedMessages);
        assertThat(user.getRoles(group.getId())).isEmpty();
    }

    @Test
    public void shouldThrownGroupNotFoundExceptionWhenRemovingRole() {
        RoleJpaRepository roleRepository = mock(RoleJpaRepository.class);
        UserJpaRepository userRepository = mock(UserJpaRepository.class);
        JpaGroupRepository groupRepository = mock(JpaGroupRepository.class);
        MessageService messageService = mock(MessageService.class);

        User creator = TestUtils.createUser();
        User user = TestUtils.createUser();
        Role role = TestUtils.createRole();
        Group group = TestUtils.createGroupWithUsers(creator, Collections.singleton(user));
        List<ErrorMessage> expectedMessages = List.of(
                new ErrorMessage("1", "message 1")
        );

        when(groupRepository.findById(group.getId()))
                .thenReturn(Optional.empty());
        when(messageService.sayError(MessageId.GROUP_NOT_FOUND, group.getId()))
                .thenReturn(expectedMessages.get(0));

        RoleServiceJpa roleService = new RoleServiceJpa(roleRepository, userRepository, groupRepository, messageService);

        DeleteException thrown = assertThrows(
                DeleteException.class,
                () -> roleService.removeRoles(group.getId(), user.getId(), role)
        );

        verify(userRepository, never()).save(user);
        assertThat(thrown.getError().isResultOperation()).isFalse();
        assertThat(thrown.getCause()).isExactlyInstanceOf(GroupNotFoundException.class);
        assertThat(new ArrayList<ErrorMessage>(thrown.getError().getMessages()))
                .containsExactlyInAnyOrderElementsOf(expectedMessages);
        assertThat(user.getRoles(group.getId())).isEmpty();
    }

    @Test
    public void shouldThrownUserNotFoundExceptionWhenRemovingRole() {
        RoleJpaRepository roleRepository = mock(RoleJpaRepository.class);
        UserJpaRepository userRepository = mock(UserJpaRepository.class);
        JpaGroupRepository groupRepository = mock(JpaGroupRepository.class);
        MessageService messageService = mock(MessageService.class);

        User creator = TestUtils.createUser();
        User user = TestUtils.createUser();
        Role role = TestUtils.createRole();
        Group group = TestUtils.createGroup(creator);
        List<ErrorMessage> expectedMessages = List.of(
                new ErrorMessage("1", "message 1")
        );

        when(groupRepository.findById(group.getId()))
                .thenReturn(Optional.of(group));
        when(messageService.sayError(MessageId.USER_NOT_FOUND_IN_GROUP, user.getId(), group.getId()))
                .thenReturn(expectedMessages.get(0));

        RoleServiceJpa roleService = new RoleServiceJpa(roleRepository, userRepository, groupRepository, messageService);

        DeleteException thrown = assertThrows(
                DeleteException.class,
                () -> roleService.removeRoles(group.getId(), user.getId(), role)
        );

        verify(userRepository, never()).save(user);
        assertThat(thrown.getError().isResultOperation()).isFalse();
        assertThat(thrown.getCause()).isExactlyInstanceOf(UserNotFoundException.class);
        assertThat(new ArrayList<ErrorMessage>(thrown.getError().getMessages()))
                .containsExactlyInAnyOrderElementsOf(expectedMessages);
        assertThat(user.getRoles(group.getId())).isEmpty();
    }

}