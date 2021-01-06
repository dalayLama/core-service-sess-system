create table roles
(
    id bigint not null
        constraint roles_pk
            primary key,
    name varchar(100) not null,
    caption varchar(100) not null,
    description text
);

create unique index roles_caption_uindex
	on roles (caption);

create unique index roles_name_uindex
	on roles (name);