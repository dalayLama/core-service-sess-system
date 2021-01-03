package com.sess.core.users;

import com.sess.core.groups.Group;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "users_id_seq"
    )
    @SequenceGenerator(
            name = "users_id_seq",
            sequenceName = "users_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "nickname", unique = true, length = 128, nullable = false)
    @NotBlank(message = "Поле \"Ник\" обязательно для заполнения")
    @Size(max = 128, message = "Поле \"Ник\" не может превышать 128 символов")
    private String nickname;

    @Column(name = "email", unique = true, length = 128, nullable = false)
    @NotBlank(message = "Поле \"email\" обязательно для заполнения")
    @Email(message = "Некорректный формат ввода поля \"email\"")
    @Size(max = 128, message = "Поле \"email\" не может превышать 128 символов")
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    @NotNull(message = "Поле \"Город\" обязательно для заполнения")
    private City city;

    @Column(name = "sex", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    @NotNull(message = "Поле \"Пол\" обязательно для заполнения")
    private Sex sex;

    @Column(name = "birthday")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @NotNull(message = "Поле \"Дата рождения\" обязательно для заполнения")
    private LocalDateTime birthday;

    @Column(name = "security_key", unique = true, length = 100, nullable = false)
    @Type(type="uuid-char")
    private UUID securityKey;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_of_groups",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "group_id") }
    )
    private List<Group> groups = new ArrayList<>();

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }

    public UUID getSecurityKey() {
        return securityKey;
    }

    public void setSecurityKey(UUID securityKey) {
        this.securityKey = securityKey;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public void addGroup(Group group) {
        groups.add(group);
    }

    public void removeGroup(long idGroup) {
        groups.stream()
                .filter(g -> Objects.equals(g.getId(), idGroup))
                .findFirst()
                .ifPresent(g -> groups.remove(g));
    }

}
