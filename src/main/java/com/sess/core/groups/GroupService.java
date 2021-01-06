package com.sess.core.groups;

import com.sess.core.exceptions.DeleteException;
import com.sess.core.exceptions.SaveException;

import java.util.List;
import java.util.Optional;

public interface GroupService {

    Group createGroup(Group group) throws SaveException;

    void deleteGroup(long idGroup) throws DeleteException;

    void updateGroup(Group group) throws SaveException;

    void addUserInGroup(long groupId, long userId) throws SaveException;

    void deleteUserFromGroup(long groupId, long userId) throws DeleteException;

    List<Group> getAllByCity(long cityId);

    Optional<Group> findById(long groupId);

}
