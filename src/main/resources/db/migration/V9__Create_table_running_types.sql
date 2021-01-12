create table running_types
(
    id bigserial not null
        constraint running_types_pk
            primary key,
    group_id bigint not null
        constraint running_types_groups_id_fk
            references groups
            on update cascade on delete restrict,
    caption varchar(100) not null,
    deleted boolean default false
);