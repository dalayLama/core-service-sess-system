package com.sess.core.groups;

import com.sess.core.roles.Role;
import com.sess.core.users.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user_of_groups")
public class UserOfGroup {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_id", updatable = false, insertable = false)
    private Group group;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "roles_users",
            joinColumns = { @JoinColumn(name = "user_of_group_id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id") }
    )
    private List<Role> roles = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(long roleId) {
        roles.stream()
                .filter(r -> Objects.equals(r.getId(), roleId))
                .findFirst()
                .ifPresent(r -> roles.remove(r));
    }

}
