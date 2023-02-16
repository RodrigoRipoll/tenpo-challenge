create table IF NOT EXISTS Trace(
    id serial primary key,
    endpoint varchar(100) not null,
    statusCode varchar(255) not null,
    response TEXT
);
