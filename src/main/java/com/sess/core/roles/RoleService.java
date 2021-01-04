package com.sess.core.roles;

import com.sess.core.exceptions.DeleteException;
import com.sess.core.exceptions.SaveException;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface RoleService {

    void addRoles(long groupId, long userId, Role... roles) throws SaveException;

    void addRoles(long groupId, long userId, Collection<? extends Role> roles) throws SaveException;

    void addRoles(long groupId, long userId, Set<Long> rolesIds) throws SaveException;

    void removeRoles(long groupId, long userId, Role... roles) throws DeleteException;

    void removeRoles(long groupId, long userId, Set<Long> rolesIds) throws DeleteException;

    void removeRoles(long groupId, long userId, Collection<? extends Role> roles) throws DeleteException;

    List<Role> getAllRoles();

}