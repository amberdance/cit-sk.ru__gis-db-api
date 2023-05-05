 alter table if exists users 
       drop constraint if exists FKp0utx8kvsuc78nb39dgyg6oko;

drop table if exists organizations cascade;
drop table if exists user_types cascade;
drop table if exists users cascade;

create table organizations (
   id bigserial not null,
    address varchar(255) not null,
    full_name varchar(255) not null,
    short_name varchar(255) not null,
    requisites varchar(255),
    primary key (id)
);

create table user_types (
   id bigserial not null,
    type varchar(20) not null,
    primary key (id)
);

create table users (
   id bigserial not null,
    chat_id varchar(50) not null,
    email varchar(50),
    phone varchar(12),
    user_type_id bigint not null,
    primary key (id)
);

alter table if exists organizations
   add constraint UK_qx8wup2ds7yfo1sc70uvxhke7 unique (address);

alter table if exists organizations
   add constraint UK_t2biihtqyq19ah12tfmy26c38 unique (short_name);

alter table if exists organizations
   add constraint UK_npjx5h9nle19qme23u7o27df unique (full_name);

alter table if exists organizations
   add constraint UK_sevkwy47p75bq0ssmguqo5u5x unique (requisites);

alter table if exists user_types
   add constraint UK_atm3n7jw9x4d6nina1c7tul96 unique (type);

alter table if exists users
   add constraint UK_nr2rmfhq6wfp39vcduy7iketb unique (chat_id);

alter table if exists users
   add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email);

alter table if exists users
   add constraint UK_du5v5sr43g5bfnji4vb8hg5s3 unique (phone);

alter table if exists users 
   add constraint FKp0utx8kvsuc78nb39dgyg6oko 
   foreign key (user_type_id) 
   references user_types;