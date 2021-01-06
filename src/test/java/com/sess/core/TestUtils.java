package com.sess.core;

import com.sess.core.dto.DTOCity;
import com.sess.core.dto.DTOUser;
import com.sess.core.groups.Group;
import com.sess.core.groups.UserOfGroup;
import com.sess.core.roles.Role;
import com.sess.core.cities.City;
import com.sess.core.users.Sex;
import com.sess.core.users.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

public abstract class TestUtils {

    private static long ID_USER = 1;

    private static long ID_GROUP = 1;

    private static long CITY_ID = 1;

    private static long ROLE_ID = 1;

    private static long USER_OF_GROUP_ID = 1;

    private TestUtils() {}

    public static Role createRole() {
        Role role = new Role();
        role.setId(newRoleId());
        role.setName(String.format("name%d", role.getId()));
        role.setDescription(String.format("description%d", role.getId()));
        role.setCaption(String.format("caption%d", role.getId()));
        return role;
    }

    public static Group createNewGroup(User creator) {
        Group group = createGroup(creator);
        group.setId(null);
        return group;
    }

    public static Group createGroup(User creator) {
        long id = newGroupId();
        City city = createCity();
        Group group = new Group();
        group.setId(id);
        group.setTitle(String.format("title%d", id));
        group.setCity(city);
        group.setCreator(creator);
        return group;
    }

    public static Group createGroupWithUsers(User creator, Collection<? extends User> users) {
        Group group = createGroup(creator);
        group.addUsers(users);
        users.forEach(u -> {
            u.addGroup(group);
            UserOfGroup userOfGroup = new UserOfGroup();
            userOfGroup.setId(newUserOfGroupId());
            userOfGroup.setGroup(group);
            userOfGroup.setUser(u);
            u.getUserOfGroups().add(userOfGroup);
        });
        return group;
    }

    public static void copy(Group target, Group source) {
        target.setId(source.getId());
        target.setTitle(source.getTitle());
        target.setCity(source.getCity());
        target.setCreator(source.getCreator());
        target.setDescription(source.getDescription());
        target.setUsers(source.getUsers());
        target.setDeleted(source.isDeleted());
    }

    public static Group createCopy(Group source) {
        Group group = new Group();
        copy(group, source);
        return group;
    }

    public static User createNewUser() {
        User user = createUser();
        user.setId(null);
        return user;
    }

    public static User createUser() {
        long newId = newId();
        User user = new User();
        user.setId(newId);
        user.setNickname(String.format("nick%d", newId));
        user.setEmail(String.format("email%d@mail.ru", newId));
        user.setSex(Sex.MALE);
        user.setCity(createCity());
        user.setSecurityKey(UUID.randomUUID());
        user.setBirthday(LocalDateTime.now());
        return user;
    }

    public static DTOUser createDTOUser(Long id) {
        return new DTOUser(
                id,
                String.format("nick%d", id),
                String.format("email%d@mail.ru", id),
                createDTOCity(),
                Sex.MALE,
                LocalDateTime.now(),
                UUID.randomUUID()
        );
    }

    public static DTOCity createDTOCity() {
        return new DTOCity(
                1L,
                "address"
        );
    }

    public static City createCity() {
        City city = new City();
        city.setId(newCityId());
        city.setAddress("address");
        return city;
    }

    private static long newId() {
        return ID_USER++;
    }

    private static long newGroupId() {
        return ID_GROUP++;
    }

    private static long newCityId() {
        return CITY_ID++;
    }

    private static long newRoleId() {
        return ROLE_ID++;
    }

    private static long newUserOfGroupId() {
        return USER_OF_GROUP_ID++;
    }

}
