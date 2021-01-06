package com.sess.core.groups;

import com.sess.core.components.message.MessageId;
import com.sess.core.components.message.MessageService;
import com.sess.core.components.validator.Validator;
import com.sess.core.dao.repositories.JpaGroupRepository;
import com.sess.core.dao.repositories.UserOfGroupJpaRepository;
import com.sess.core.groups.exceptions.GroupNotFoundException;
import com.sess.core.roles.RoleService;
import com.sess.core.users.exceptions.UserNotFoundException;
import com.sess.core.exceptions.*;
import com.sess.core.groups.exceptions.UserAlreadyExistInGroup;
import com.sess.core.users.registration.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class JpaGroupService implements GroupService {

    private static final Logger LOG = LoggerFactory.getLogger(JpaGroupService.class);

    private final JpaGroupRepository groupRepository;

    private final MessageService messageService;

    private final UserService userService;

    private final UserOfGroupJpaRepository userOfGroupRepository;

    private final Validator validator;

    private final RoleService roleService;

    public JpaGroupService(
            JpaGroupRepository groupRepository,
            MessageService messageService,
            UserService userService,
            UserOfGroupJpaRepository userOfGroupRepository,
            Validator validator,
            RoleService roleService) {
        this.groupRepository = groupRepository;
        this.messageService = messageService;
        this.userService = userService;
        this.userOfGroupRepository = userOfGroupRepository;
        this.validator = validator;
        this.roleService = roleService;
    }

    @Override
    @Transactional
    public Group createGroup(Group group) throws SaveException {
        if (Objects.nonNull(group.getId())) {
            ErrorMessage errorMessage = messageService.sayError(MessageId.NOT_NULLABLE_GROUP_ID);
            throw new NotNullableId(errorMessage);
        }
        validate(group);
        Group savedGroup = groupRepository.saveAndFlush(group);
        addUserInGroup(savedGroup, savedGroup.getCreator().getId());
        roleService.addRoles(savedGroup.getId(), savedGroup.getCreator().getId(), roleService.getAllRoles());
        return savedGroup;
    }

    @Override
    @Transactional
    public void deleteGroup(long idGroup) throws DeleteException {
        Optional<Group> byId = groupRepository.findById(idGroup);
        byId.ifPresentOrElse(
                group -> {
                    group.setDeleted(true);
                    group.getUsers().forEach(u -> u.removeGroup(group.getId()));
                    group.getUsers().clear();
                    groupRepository.save(group);
                },
                () -> LOG.warn("group({}) wasn't found", idGroup)
        );
    }

    @Override
    public void updateGroup(Group group) throws UpdateException {
        Optional<Group> byId = groupRepository.findById(group.getId());
        byId.ifPresentOrElse(
                g -> {
                    updateGroup(g, group);
                    validate(g);
                    groupRepository.save(g);
                },
                () -> {
                    GroupNotFoundException exc = createGroupNotFoundException(group.getId());
                    throw new UpdateException(exc, exc.getError());
                }
        );
    }

    @Override
    @Transactional
    public void addUserInGroup(long groupId, long userId) throws SaveException {
        if (checkExistUserInGroup(groupId, userId)) {
            ErrorMessage errorMessage = messageService.sayError(MessageId.USER_ALREADY_EXIST_IN_GROUP, userId, groupId);
            throw new UserAlreadyExistInGroup(errorMessage);
        }
        groupRepository.findById(groupId).ifPresentOrElse(
                group -> addUserInGroup(group, userId),
                () -> {
                    GroupNotFoundException exc = createGroupNotFoundException(groupId);
                    throw new SaveException(exc, exc.getError());
                }
        );
    }

    @Override
    @Transactional
    public void deleteUserFromGroup(long groupId, long userId) {
        if (!checkExistUserInGroup(groupId, userId)) {
            LOG.warn("user({}) wasn't found in group({})", userId, groupId);
            return;
        }
        groupRepository.findById(groupId).ifPresent(
                group -> group.getUsers().stream()
                        .filter(user -> Objects.equals(user.getId(), userId))
                        .findFirst()
                        .ifPresent(user -> {
                            group.removeUser(user.getId());
                            user.removeGroup(group.getId());
                            groupRepository.save(group);
                        })
        );
    }

    @Override
    @Transactional
    public List<Group> getAllByCity(long cityId) {
        return groupRepository.findAllByCityIdAndDeletedFalse(cityId);
    }

    @Override
    @Transactional
    public Optional<Group> findById(long groupId) {
        return groupRepository.findById(groupId);
    }

    private void updateGroup(Group target, Group source) {
        target.setTitle(source.getTitle());
        target.setCity(source.getCity());
        target.setDescription(source.getDescription());
    }

    private void addUserInGroup(Group group, long userId) {
        userService.findById(userId).ifPresentOrElse(
                user -> {
                    group.addUser(user);
                    user.addGroup(group);
                    groupRepository.saveAndFlush(group);
                },
                () -> {
                    ErrorMessage errorMessage = messageService.sayError(MessageId.USER_NOT_FOUND, userId);
                    UserNotFoundException exc = new UserNotFoundException(errorMessage);
                    throw new SaveException(exc, exc.getError());
                }
        );
    }

    private void validate(Group group) throws ValidationException {
        Set<String> result = validator.validate(group);
        Optional<Group> existedGroup = Objects.isNull(group.getId()) ?
                groupRepository.findByTitleAndDeletedFalse(group.getTitle()) :
                groupRepository.findByTitleAndDeletedFalse(group.getId(), group.getTitle());
        existedGroup.ifPresent(g -> {
            String text = messageService.say(MessageId.GROUP_TITLE_ALREADY_EXISTS);
            result.add(text);
        });
        if (!result.isEmpty()) {
            throw new ValidationException(result);
        }
    }

    private boolean checkExistUserInGroup(long groupId, long userId) {
        return userOfGroupRepository.existsByGroupIdAndUserId(groupId, userId);
    }

    private GroupNotFoundException createGroupNotFoundException(long idGroup) {
        ErrorMessage errorMessage = messageService.sayError(MessageId.GROUP_NOT_FOUND, idGroup);
        return new GroupNotFoundException(errorMessage);
    }

}
