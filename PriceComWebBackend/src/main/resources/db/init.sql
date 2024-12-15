drop table if exists `user`;
drop table if exists `history`;
drop table if exists `jd_items`;
drop table if exists `sn_items`;
drop table if exists `wph_items`;
drop table if exists `reminder`;

create table if not exists `user` (
    `id` int not null auto_increment ,
    `username` varchar(25) unique not null ,
    `password` varchar(65) not null ,
    `email` varchar(64) unique not null ,
    `phone` varchar(15) default null,
    `address` varchar(64) default null,
    primary key (`id`)
);

create table if not exists `history` (
    `id` int not null auto_increment ,
    `user_id` int not null ,
    `content` varchar(256) not null ,
    `time` datetime not null default current_timestamp ,
    primary key (`id`)
);

create table if not exists `jd_items` (
    `id` int not null auto_increment ,
    `price` decimal(10, 2) not null ,
    `description` varchar(255) not null ,
    `img` varchar(255) not null ,
    `category` varchar(255) default null ,
    `time` datetime not null default current_timestamp,
    `shop_name` varchar(255) default null ,
    `query_name` varchar(255) not null ,
    `detail_url` varchar(255) default null ,
    primary key (`id`)
);

create table if not exists `sn_items` (
    `id` int not null auto_increment ,
    `price` decimal(10, 2) not null default 0,
    `description` varchar(255) not null ,
    `img` varchar(255) not null ,
    `category` varchar(255) default null ,
    `time` datetime not null default current_timestamp,
    `shop_name` varchar(255) default null ,
    `query_name` varchar(255) not null ,
    `detail_url` varchar(2047) default null ,
    primary key (`id`)
);

create table if not exists `wph_items` (
    `id` int not null auto_increment ,
    `price` decimal(10, 2) not null default 0,
    `description` varchar(255) not null ,
    `img` varchar(255) not null ,
    `category` varchar(255) default null ,
    `time` datetime not null default current_timestamp,
    `shop_name` varchar(255) default null ,
    `query_name` varchar(255) not null ,
    `detail_url` varchar(255) default null ,
    primary key (`id`)
);

create table if not exists `reminder` (
    `id` int not null auto_increment ,
    `user_id` int not null ,
    `description` varchar(255) not null ,
    `price` decimal(10, 2) not null default 0,
    `img` varchar(255) not null ,
    `detail_url` varchar(2047) default null ,
    `from` varchar(10) not null ,
    primary key (`id`)
)