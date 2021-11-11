create table black_list
(
    id   int auto_increment
        primary key,
    ip   varchar(32) not null,
    time datetime    not null,
    constraint ip
        unique (ip)
);