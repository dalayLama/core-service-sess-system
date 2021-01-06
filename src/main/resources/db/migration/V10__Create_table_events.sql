create table event
(
    id bigserial not null
        constraint event_pk
            primary key,
    group_id bigint not null
        constraint event_groups_id_fk
            references groups
            on update cascade on delete restrict,
    event_type_id bigint not null
        constraint event_event_types_id_fk
            references event_types
            on update cascade on delete restrict,
    creator_id bigint not null
        constraint event_users_id_fk
            references users
            on update cascade on delete restrict,
    event_name varchar(100),
    place_start varchar(255) not null,
    place_end varchar(255) not null,
    planned_dt_start timestamp not null,
    planned_dt_end timestamp not null,
    description text,
    factual_dt_start timestamp,
    factual_dt_ent timestamp
);