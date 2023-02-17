DROP TABLE IF EXISTS Request_Log;

CREATE TABLE Request_Log(
    id serial primary key,
    timestamp TIMESTAMP not null ,
    method varchar(20) not null,
    uri varchar(255) not null,
    status_code SMALLINT not null,
    response JSONB
);
