
--drop table if exists configuration;

create table configuration (
    key varchar(250) primary key not null,
    value json
);
