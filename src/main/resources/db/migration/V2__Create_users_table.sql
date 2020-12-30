create table users
(
    id bigserial not null
        constraint users_pk
            primary key,
    nickname varchar(128) not null,
    email varchar(128) not null,
    sex numeric(1) not null,
    birthday timestamp,
    city_id bigint not null
        constraint users_cities_id_fk
            references cities
            on update cascade on delete restrict,
    security_key varchar(100) not null
);

create unique index users_email_uindex
    on users (email);

create unique index users_nickname_uindex
    on users (nickname);

create unique index users_security_key_uinex
    on users (security_key);