package com.sess.core.roles;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleServiceJpa implements RoleService {

    private static final Logger LOG = LoggerFactory.getLogger(RoleServiceJpa.class);

    private final RoleJpaRepository roleRepository;

    private final UserJpaRepository userRepository;

    private final JpaGroupRepository groupRepository;

    private final MessageService messageService;

    public RoleServiceJpa(
            RoleJpaRepository roleRepository,
            UserJpaRepository userRepository,
            JpaGroupRepository groupRepository,
            MessageService messageService) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.messageService = messageService;
    }

    @Override
    public void addRoles(long groupId, long userId, Role... roles) throws SaveException {
        addRoles(groupId, userId, Arrays.asList(roles));
    }

    @Override
    public void addRoles(long groupId, long userId, Collection<? extends Role> roles) throws SaveException {
        groupRepository.findById(groupId).ifPresentOrElse(
                group -> addRole(group, userId, roles),
                () -> {
                    ErrorMessage errorMessage = messageService.sayError(MessageId.GROUP_NOT_FOUND, groupId);
                    GroupNotFoundException exc = new GroupNotFoundException(errorMessage);
                    throw new SaveException(exc, exc.getError());
                }
        );
    }

    @Override
    public void addRoles(long groupId, long userId, Set<Long> rolesIds) throws SaveException {
        List<Role> allById = roleRepository.findAllById(rolesIds);
        addRoles(groupId, userId, allById);
    }

    @Override
    public void removeRoles(long groupId, long userId, Role... roles) throws DeleteException {
        removeRoles(groupId, userId, Arrays.asList(roles));
    }

    @Override
    public void removeRoles(long groupId, long userId, Set<Long> rolesIds) throws DeleteException {
        List<Role> allById = roleRepository.findAllById(rolesIds);
        removeRoles(groupId, userId, allById);
    }

    @Override
    public void removeRoles(long groupId, long userId, Collection<? extends Role> roles) throws DeleteException {
        groupRepository.findById(groupId).ifPresentOrElse(
                group -> removeRoles(group, userId, roles),
                () -> {
                    ErrorMessage errorMessage = messageService.sayError(MessageId.GROUP_NOT_FOUND, groupId);
                    GroupNotFoundException exc = new GroupNotFoundException(errorMessage);
                    throw new DeleteException(exc, exc.getError());
                }
        );
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    private void addRole(Group group, long userId, Collection<? extends Role> roles) {
        group.getUsers().stream()
                .filter(u -> Objects.equals(u.getId(), userId))
                .findFirst()
                .ifPresentOrElse(
                        user -> addRole(group, user, roles),
                        () -> {
                            ErrorMessage errorMessage = messageService.sayError(
                                    MessageId.USER_NOT_FOUND_IN_GROUP, userId, group.getId());
                            UserNotFoundException exc = new UserNotFoundException(errorMessage);
                            throw new SaveException(exc, exc.getError());
                        }
                );
    }

    private void addRole(Group group, User user, Collection<? extends Role> roles) {
        roles.forEach(role -> {
            user.getRoles(group.getId()).stream()
                    .filter(r -> Objects.equals(role.getId(), r.getId()))
                    .findFirst()
                    .ifPresentOrElse(
                            existedRole -> LOG.warn("user({}) already has role({})", user.getId(), role.getId()),
                            () -> user.addRole(group.getId(), role)
                    );
        });
        userRepository.save(user);
    }

    private void removeRoles(Group group, long userId, Collection<? extends Role> roles) {
        group.getUsers().stream()
                .filter(u -> Objects.equals(u.getId(), userId))
                .findFirst()
                .ifPresentOrElse(
                        user -> removeRoles(group, user, roles),
                        () -> {
                            ErrorMessage errorMessage = messageService.sayError(
                                    MessageId.USER_NOT_FOUND_IN_GROUP, userId, group.getId());
                            UserNotFoundException exc = new UserNotFoundException(errorMessage);
                            throw new DeleteException(exc, exc.getError());
                        }
                );
    }

    private void removeRoles(Group group, User user, Collection<? extends Role> roles)
    {
        roles.forEach(role -> {
            user.getRoles(group.getId()).stream()
                    .filter(r -> Objects.equals(r.getId(), role.getId()))
                    .findFirst()
                    .ifPresentOrElse(
                            existedRole -> user.removeRole(group.getId(), existedRole.getId()),
                            () -> LOG.warn("user({}) doesn't have role({})", user.getId(), role.getId())
                    );
        });
        userRepository.save(user);
    }

}
