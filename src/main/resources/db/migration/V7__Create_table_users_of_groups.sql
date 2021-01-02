create table user_of_groups
(
    id bigserial not null
        constraint user_of_groups_pk
            primary key,
    group_id bigint not null
        constraint user_of_groups_groups_id_fk
            references groups
            on update cascade on delete cascade,
    user_id bigint not null
        constraint user_of_groups_users_id_fk
            references users
            on update cascade on delete cascade,
    constraint user_of_groups_uk
        unique (group_id, user_id)
);