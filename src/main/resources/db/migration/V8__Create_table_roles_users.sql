create table roles_users
(
    id bigserial not null
        constraint roles_users_pk
            primary key,
    user_of_group_id bigint not null
        constraint roles_users_user_of_groups_id_fk
            references user_of_groups
            on update cascade on delete cascade,
    role_id bigint not null,
    constraint roles_users_uk
        unique (user_of_group_id, role_id)
);