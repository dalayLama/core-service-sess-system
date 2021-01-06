create table event_types
(
    id bigint not null
        constraint event_types_pk
            primary key,
    caption varchar(100) not null
);

create unique index event_types_caption_uindex
    on event_types (caption);
