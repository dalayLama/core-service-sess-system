create table cities
(
    id               bigserial     not null
        constraint cities_pk
            primary key,
    address          varchar(1024) not null,
    postal_code      varchar(100),
    federal_district varchar(100)  not null,
    region_type      varchar(100)  not null,
    region           varchar(100)  not null,
    city             varchar(100),
    kladr_id         varchar(100)  not null,
    fias_id          varchar(100)  not null,
    fias_level       integer       not null,
    okato            varchar(100)  not null,
    oktmo            varchar(100)  not null,
    tax_office       varchar(100)  not null,
    timezone         varchar(50)   not null
);

alter table cities
    owner to postgres;

create unique index cities_address_uindex
    on cities (address);

create unique index cities_fias_id_uindex
    on cities (fias_id);

create unique index cities_kladr_id_uindex
    on cities (kladr_id);