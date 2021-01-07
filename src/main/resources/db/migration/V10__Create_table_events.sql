create table events
(
    id bigserial not null
        constraint events_pk
            primary key,
    group_id bigint not null
        constraint events_groups_id_fk
            references groups
            on update cascade on delete restrict,
    running_type_id bigint
        constraint events_running_types_id_fk
            references running_types
            on update cascade on delete restrict,
    event_name varchar(100),
    distance numeric(8, 2) not null,
    place_start varchar(255) not null,
    place_end varchar(255) not null,
    planned_dt_start timestamp not null,
    planned_dt_end timestamp not null,
    description text,
    factual_dt_start timestamp,
    factual_dt_end timestamp
);