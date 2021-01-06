create table groups
(
    id bigserial not null
        constraint groups_pk
            primary key,
    city_id bigint not null
        constraint groups_cities_id_fk
            references cities
            on update cascade on delete restrict,
    creator_id bigint not null
        constraint groups_users_id_fk
            references users
            on update cascade on delete restrict,
    title varchar(100) not null,
    description text,
    deleted boolean default false not null
);