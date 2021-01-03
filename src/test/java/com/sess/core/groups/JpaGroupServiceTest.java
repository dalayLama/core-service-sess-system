package com.sess.core.groups;

import com.sess.core.ErrorsCodes;
import com.sess.core.TestUtils;
import com.sess.core.components.message.MessageId;
import com.sess.core.components.message.MessageService;
import com.sess.core.components.validator.Validator;
import com.sess.core.dao.repositories.JpaGroupRepository;
import com.sess.core.dao.repositories.UserOfGroupJpaRepository;
import com.sess.core.groups.exceptions.GroupNotFoundException;
import com.sess.core.users.exceptions.UserNotFoundException;
import com.sess.core.exceptions.*;
import com.sess.core.groups.exceptions.UserAlreadyExistInGroup;
import com.sess.core.users.User;
import com.sess.core.users.registration.UserService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.sess.core.components.message.MessageId.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class JpaGroupServiceTest {

    @Test
    public void shouldExecuteMethodsInExpectedOrder() {
        JpaGroupRepository groupRepository = mock(JpaGroupRepository.class);
        MessageService messageService = mock(MessageService.class);
        UserService userService = mock(UserService.class);
        UserOfGroupJpaRepository userOfGroupJpaRepository = mock(UserOfGroupJpaRepository.class);
        Validator validator = mock(Validator.class);
        Group newGroup = TestUtils.createNewGroup(TestUtils.createUser());

        when(groupRepository.findByTitleAndDeletedFalse(newGroup.getTitle())).thenReturn(Optional.empty());
        when(groupRepository.save(newGroup)).thenReturn(TestUtils.createGroup(newGroup.getCreator()));

        JpaGroupService groupService = new JpaGroupService(
                groupRepository, messageService, userService, userOfGroupJpaRepository, validator);

        Group group = groupService.createGroup(newGroup);
        assertThat(group.getId()).isNotNull();
        verify(validator).validate(newGroup);
    }

    @Test
    public void shouldThrownNotNullableIdException() {
        JpaGroupRepository groupRepository = mock(JpaGroupRepository.class);
        MessageService messageService = mock(MessageService.class);
        UserService userService = mock(UserService.class);
        UserOfGroupJpaRepository userOfGroupJpaRepository = mock(UserOfGroupJpaRepository.class);
        Validator validator = mock(Validator.class);
        Group group = new Group();
        group.setId(1L);

        List<ErrorMessage> expectedMessages = List.of(
                new ErrorMessage("1", "message 1")
        );
        when(messageService.sayError(MessageId.NOT_NULLABLE_GROUP_ID))
                .thenReturn(expectedMessages.get(0));

        JpaGroupService groupService = new JpaGroupService(
                groupRepository, messageService, userService, userOfGroupJpaRepository, validator);

        NotNullableId thrown = assertThrows(
                NotNullableId.class,
                () -> groupService.createGroup(group)
        );
        assertThat(thrown.getError().isResultOperation()).isFalse();
        assertThat(new ArrayList<ErrorMessage>(thrown.getError().getMessages()))
                .containsExactlyInAnyOrderElementsOf(expectedMessages);
        verify(validator, never()).validate(any());
        verify(groupRepository, never()).findByTitleAndDeletedFalse(any());
        verify(groupRepository, never()).findByTitleAndDeletedFalse(anyInt(), any());
        verify(groupRepository, never()).save(any());
    }

    @Test
    public void shouldThrownValidationException() {
        JpaGroupRepository groupRepository = mock(JpaGroupRepository.class);
        MessageService messageService = mock(MessageService.class);
        UserService userService = mock(UserService.class);
        UserOfGroupJpaRepository userOfGroupJpaRepository = mock(UserOfGroupJpaRepository.class);
        Validator validator = mock(Validator.class);
        Group group = TestUtils.createNewGroup(TestUtils.createUser());

        List<ErrorMessage> expectedMessages = List.of(
                new ErrorMessage(ErrorsCodes.VALIDATE_ERROR, "message "),
                new ErrorMessage(ErrorsCodes.VALIDATE_ERROR, "message 2"),
                new ErrorMessage(ErrorsCodes.VALIDATE_ERROR, "title exception")
        );

        when(validator.validate(group)).thenReturn(
                expectedMessages.stream()
                        .map(ErrorMessage::getMessage)
                        .filter(message -> !message.equals("title exception"))
                        .collect(Collectors.toSet())
        );
        when(groupRepository.findByTitleAndDeletedFalse(group.getTitle()))
                .thenReturn(Optional.of(TestUtils.createGroup(TestUtils.createUser())));
        when(messageService.say(GROUP_TITLE_ALREADY_EXISTS)).thenReturn(
                expectedMessages.get(2).getMessage()
        );


        JpaGroupService groupService = new JpaGroupService(
                groupRepository, messageService, userService, userOfGroupJpaRepository, validator);

        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> groupService.createGroup(group)
        );
        verify(groupRepository, never()).findByTitleAndDeletedFalse(anyLong(), any());
        verify(groupRepository, never()).save(any());
        assertThat(thrown.getError().isResultOperation()).isFalse();
        assertThat(new ArrayList<ErrorMessage>(thrown.getError().getMessages()))
                .containsExactlyInAnyOrderElementsOf(expectedMessages);
    }

    @Test
    public void shouldDeleteGroupCorrect() {
        JpaGroupRepository groupRepository = mock(JpaGroupRepository.class);
        MessageService messageService = mock(MessageService.class);
        UserService userService = mock(UserService.class);
        UserOfGroupJpaRepository userOfGroupJpaRepository = mock(UserOfGroupJpaRepository.class);
        Validator validator = mock(Validator.class);
        List<User> usersOfGroup = List.of(
                TestUtils.createUser(),
                TestUtils.createUser(),
                TestUtils.createUser()
        );
        Group group = TestUtils.createGroupWithUsers(
                TestUtils.createUser(),
                usersOfGroup
        );

        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));

        JpaGroupService groupService = new JpaGroupService(
                groupRepository, messageService, userService, userOfGroupJpaRepository, validator);
        groupService.deleteGroup(group.getId());

        assertThat(group.isDeleted()).isTrue();
        assertThat(group.getUsers()).isEmpty();
        for (User user : usersOfGroup) {
            List<Long> groupsOfUser = user.getGroups().stream().map(Group::getId).collect(Collectors.toList());
            assertThat(groupsOfUser).doesNotContain(group.getId());
        }
        verify(groupRepository).save(group);
    }

    @Test
    public void shouldHappenNothing() {
        JpaGroupRepository groupRepository = mock(JpaGroupRepository.class);
        MessageService messageService = mock(MessageService.class);
        UserService userService = mock(UserService.class);
        UserOfGroupJpaRepository userOfGroupJpaRepository = mock(UserOfGroupJpaRepository.class);
        Validator validator = mock(Validator.class);
        long groupId = 88L;
        List<ErrorMessage> expectedMessages = List.of(
                new ErrorMessage("1", "message 1")
        );

        when(groupRepository.findById(groupId))
                .thenReturn(Optional.empty());
        when(messageService.sayError(GROUP_NOT_FOUND))
                .thenReturn(expectedMessages.get(0));

        JpaGroupService groupService = new JpaGroupService(
                groupRepository, messageService, userService, userOfGroupJpaRepository, validator);
        groupService.deleteGroup(groupId);

        groupService.deleteGroup(groupId);
        verify(groupRepository, never()).save(any());
    }

    @Test
    public void shouldUpdateCorrect() {
        JpaGroupRepository groupRepository = mock(JpaGroupRepository.class);
        MessageService messageService = mock(MessageService.class);
        UserService userService = mock(UserService.class);
        UserOfGroupJpaRepository userOfGroupJpaRepository = mock(UserOfGroupJpaRepository.class);
        Validator validator = mock(Validator.class);
        Group oldData = TestUtils.createGroup(TestUtils.createUser());
        Group newData = TestUtils.createCopy(oldData);
        newData.setDescription("big new text");
        newData.setTitle("unusual new title");
        newData.setCity(TestUtils.createCity());

        when(groupRepository.findById(newData.getId()))
                .thenReturn(Optional.of(oldData));

        JpaGroupService groupService = new JpaGroupService(
                groupRepository, messageService, userService, userOfGroupJpaRepository, validator);
        groupService.updateGroup(newData);

        verify(validator).validate(oldData);
        verify(groupRepository).findByTitleAndDeletedFalse(oldData.getId(), newData.getTitle());
        verify(groupRepository).save(oldData);
        assertThat(oldData.getTitle()).isEqualTo(newData.getTitle());
        assertThat(oldData.getDescription()).isEqualTo(newData.getDescription());
        assertThat(oldData.getCity()).isSameAs(newData.getCity());
    }

    @Test
    public void shouldThrownGroupNotFoundException() {
        JpaGroupRepository groupRepository = mock(JpaGroupRepository.class);
        MessageService messageService = mock(MessageService.class);
        UserService userService = mock(UserService.class);
        UserOfGroupJpaRepository userOfGroupJpaRepository = mock(UserOfGroupJpaRepository.class);
        Validator validator = mock(Validator.class);
        Group group = TestUtils.createGroup(TestUtils.createUser());

        List<ErrorMessage> expectedMessages = List.of(
                new ErrorMessage("1", "message ")
        );

        when(groupRepository.findById(group.getId())).thenReturn(Optional.empty());
        when(messageService.sayError(GROUP_NOT_FOUND, group.getId())).thenReturn(
                expectedMessages.get(0)
        );

        JpaGroupService groupService = new JpaGroupService(
                groupRepository, messageService, userService, userOfGroupJpaRepository, validator);

        UpdateException thrown = assertThrows(
                UpdateException.class,
                () -> groupService.updateGroup(group)
        );
        verify(validator, never()).validate(any());
        verify(groupRepository, never()).findByTitleAndDeletedFalse(anyLong(), anyString());
        verify(groupRepository, never()).save(any());
        assertThat(thrown.getError().isResultOperation()).isFalse();
        assertThat(thrown.getCause()).isExactlyInstanceOf(GroupNotFoundException.class);
        assertThat(new ArrayList<ErrorMessage>(thrown.getError().getMessages()))
                .containsExactlyInAnyOrderElementsOf(expectedMessages);
    }

    @Test
    public void shouldThrownUserAlreadyExistInGroup() {
        JpaGroupRepository groupRepository = mock(JpaGroupRepository.class);
        MessageService messageService = mock(MessageService.class);
        UserService userService = mock(UserService.class);
        UserOfGroupJpaRepository userOfGroupJpaRepository = mock(UserOfGroupJpaRepository.class);
        Validator validator = mock(Validator.class);
        Group group = TestUtils.createGroup(TestUtils.createUser());
        User user = TestUtils.createUser();

        List<ErrorMessage> expectedMessages = List.of(
                new ErrorMessage("1", "message ")
        );

        when(userOfGroupJpaRepository.existsByGroupIdAndUserId(group.getId(), user.getId()))
                .thenReturn(true);
        when(messageService.sayError(USER_ALREADY_EXIST_IN_GROUP, user.getId(), group.getId())).thenReturn(
                expectedMessages.get(0)
        );

        JpaGroupService groupService = new JpaGroupService(
                groupRepository, messageService, userService, userOfGroupJpaRepository, validator);

        UserAlreadyExistInGroup thrown = assertThrows(
                UserAlreadyExistInGroup.class,
                () -> groupService.addUserInGroup(group.getId(), user.getId())
        );
        verify(validator, never()).validate(any());
        verify(groupRepository, never()).save(any());
        assertThat(thrown.getError().isResultOperation()).isFalse();
        assertThat(new ArrayList<ErrorMessage>(thrown.getError().getMessages()))
                .containsExactlyInAnyOrderElementsOf(expectedMessages);
    }

    @Test
    public void shouldAddUserInGroup() {
        JpaGroupRepository groupRepository = mock(JpaGroupRepository.class);
        MessageService messageService = mock(MessageService.class);
        UserService userService = mock(UserService.class);
        UserOfGroupJpaRepository userOfGroupJpaRepository = mock(UserOfGroupJpaRepository.class);
        Validator validator = mock(Validator.class);
        Group group = TestUtils.createGroup(TestUtils.createUser());
        User user = TestUtils.createUser();

        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(userService.findById(user.getId())).thenReturn(Optional.of(user));

        JpaGroupService groupService = new JpaGroupService(
                groupRepository, messageService, userService, userOfGroupJpaRepository, validator);

        groupService.addUserInGroup(group.getId(), user.getId());

        verify(groupRepository).save(group);
        verify(userOfGroupJpaRepository).existsByGroupIdAndUserId(group.getId(), user.getId());
        verify(validator, never()).validate(any());
        List<Long> usersIdsInGroup = group.getUsers().stream().map(User::getId).collect(Collectors.toList());
        List<Long> groupsIdsInUser = user.getGroups().stream().map(Group::getId).collect(Collectors.toList());
        assertThat(usersIdsInGroup).containsExactlyInAnyOrderElementsOf(Collections.singleton(user.getId()));
        assertThat(groupsIdsInUser).containsExactlyInAnyOrderElementsOf(Collections.singleton(group.getId()));
    }


    @Test
    public void shouldThrownUserNotFoundException() {
        JpaGroupRepository groupRepository = mock(JpaGroupRepository.class);
        MessageService messageService = mock(MessageService.class);
        UserService userService = mock(UserService.class);
        UserOfGroupJpaRepository userOfGroupJpaRepository = mock(UserOfGroupJpaRepository.class);
        Validator validator = mock(Validator.class);
        Group group = TestUtils.createGroup(TestUtils.createUser());
        long userId = 133L;


        List<ErrorMessage> expectedMessages = List.of(
                new ErrorMessage("1", "message ")
        );

        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(userService.findById(userId)).thenReturn(Optional.empty());
        when(messageService.sayError(USER_NOT_FOUND, userId)).thenReturn(expectedMessages.get(0));

        JpaGroupService groupService = new JpaGroupService(
                groupRepository, messageService, userService, userOfGroupJpaRepository, validator);

        SaveException thrown = assertThrows(
                SaveException.class,
                () -> groupService.addUserInGroup(group.getId(), userId)
        );
        verify(validator, never()).validate(any());
        verify(groupRepository, never()).save(any());
        assertThat(thrown.getError().isResultOperation()).isFalse();
        assertThat(thrown.getCause()).isExactlyInstanceOf(UserNotFoundException.class);
        assertThat(new ArrayList<ErrorMessage>(thrown.getError().getMessages()))
                .containsExactlyInAnyOrderElementsOf(expectedMessages);
    }

    @Test
    public void shouldDeleteUserFromGroup() {
        JpaGroupRepository groupRepository = mock(JpaGroupRepository.class);
        MessageService messageService = mock(MessageService.class);
        UserService userService = mock(UserService.class);
        UserOfGroupJpaRepository userOfGroupJpaRepository = mock(UserOfGroupJpaRepository.class);
        Validator validator = mock(Validator.class);
        List<User> users = List.of(
                TestUtils.createUser(),
                TestUtils.createUser(),
                TestUtils.createUser()
        );
        Group group1 = TestUtils.createGroupWithUsers(TestUtils.createUser(), users);
        Group group2 = TestUtils.createGroupWithUsers(TestUtils.createUser(), users);
        Group group3 = TestUtils.createGroupWithUsers(TestUtils.createUser(), users);


        when(groupRepository.findById(group1.getId())).thenReturn(Optional.of(group1));
        when(userOfGroupJpaRepository.existsByGroupIdAndUserId(group1.getId(), users.get(0).getId())).thenReturn(true);
        users.forEach(user -> when(userService.findById(user.getId())).thenReturn(Optional.of(user)));


        JpaGroupService groupService = new JpaGroupService(
                groupRepository, messageService, userService, userOfGroupJpaRepository, validator);

        groupService.deleteUserFromGroup(group1.getId(), users.get(0).getId());

        verify(groupRepository).save(group1);
        verify(userOfGroupJpaRepository).existsByGroupIdAndUserId(group1.getId(), users.get(0).getId());
        verify(validator, never()).validate(any());
        List<Long> usersIdsInGroup = group1.getUsers().stream().map(User::getId).collect(Collectors.toList());
        List<Long> groupsIdsInUser = users.get(0).getGroups().stream().map(Group::getId).collect(Collectors.toList());
        assertThat(usersIdsInGroup).containsExactlyInAnyOrderElementsOf(
                List.of(users.get(1), users.get(2)).stream().map(User::getId).collect(Collectors.toList())
        );
        assertThat(groupsIdsInUser).containsExactlyInAnyOrderElementsOf(
                List.of(group2, group3).stream().map(Group::getId).collect(Collectors.toList())
        );
    }

}