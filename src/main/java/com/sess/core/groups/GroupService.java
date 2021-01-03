package com.sess.core.groups;

import com.sess.core.exceptions.DeleteException;
import com.sess.core.exceptions.SaveException;

public interface GroupService {

    Group createGroup(Group group) throws SaveException;

    void deleteGroup(long idGroup) throws DeleteException;

    void updateGroup(Group group) throws SaveException;

    void addUserInGroup(long groupId, long userId) throws SaveException;

    void deleteUserFromGroup(long groupId, long userId) throws DeleteException;

}
