create table thumbnail
(
    id       int auto_increment
        primary key,
    name     varchar(64) null,
    img_list mediumtext  null,
    constraint `key`
        unique (name)
);

