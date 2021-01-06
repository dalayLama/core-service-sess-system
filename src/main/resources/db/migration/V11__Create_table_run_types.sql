create table run_types
(
    id bigserial not null
        constraint run_types_pk
            primary key,
    group_id bigint not null,
    caption varchar(100) not null
);

create unique index run_types_group_id_caption_uindex
    on run_types (group_id, caption);