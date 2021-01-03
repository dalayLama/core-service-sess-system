package com.sess.core.groups;

import com.sess.core.users.City;
import com.sess.core.users.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Group where occur events
 */
@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "groups_id_seq"
    )
    @SequenceGenerator(
            name = "groups_id_seq",
            sequenceName = "groups_id_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(name = "title", length = 100, nullable = false, unique = true)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "groups")
    private List<User> users = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User user) {
        this.creator = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
