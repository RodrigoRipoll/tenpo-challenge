DROP TABLE IF EXISTS request_log;

CREATE TABLE request_log (
    id serial primary key,
    timestamp TIMESTAMP not null ,
    method varchar(20) not null,
    uri varchar(255) not null,
    status_code SMALLINT not null,
    response JSONB
);
