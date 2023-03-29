# CREATE SCHEMA if not exists `forum`;

CREATE TABLE `user`
(
    `id`               bigint NOT NULL AUTO_INCREMENT,
    `create_date`      datetime(6)  DEFAULT NULL,
    `email`            varchar(255) DEFAULT NULL,
    `last_update_date` datetime(6)  DEFAULT NULL,
    `uuid`             varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`),
    UNIQUE KEY `UK_1xc1iry6gqjrvh5cpajiq7l2f` (`uuid`)
);

CREATE TABLE `role`
(
    `id`               bigint NOT NULL AUTO_INCREMENT,
    `create_date`      datetime(6)  DEFAULT NULL,
    `last_update_date` datetime(6)  DEFAULT NULL,
    `role`             varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_bjxn5ii7v7ygwx39et0wawu0q` (`role`)
);

CREATE TABLE `profile`
(
    `id`               bigint       NOT NULL AUTO_INCREMENT,
    `birth`            varchar(255) DEFAULT NULL,
    `create_date`      datetime(6)  DEFAULT NULL,
    `last_name`        varchar(255) DEFAULT NULL,
    `last_update_date` datetime(6)  DEFAULT NULL,
    `location`         varchar(255) DEFAULT NULL,
    `name`             varchar(255) DEFAULT NULL,
    `personal_info`    varchar(255) DEFAULT NULL,
    `uuid`             varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKp4d905n4e97iop1xo5swv8796` (`uuid`),
    CONSTRAINT `FKp4d905n4e97iop1xo5swv8796` FOREIGN KEY (`uuid`) REFERENCES `user` (`uuid`)
);
CREATE TABLE `authentication`
(
    `id`               bigint       NOT NULL AUTO_INCREMENT,
    `create_date`      datetime(6)  DEFAULT NULL,
    `last_update_date` datetime(6)  DEFAULT NULL,
    `password`         varchar(255) DEFAULT NULL,
    `uuid`             varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_64wxc2th6fiwurifppbxwc3f1` (`uuid`),
    CONSTRAINT `FKksqjn66ho6uth724c7vclvers` FOREIGN KEY (`uuid`) REFERENCES `user` (`uuid`)
);

CREATE TABLE `authorization`
(
    `id`               bigint       NOT NULL AUTO_INCREMENT,
    `create_date`      datetime(6) DEFAULT NULL,
    `last_update_date` datetime(6) DEFAULT NULL,
    `uuid`             varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_nyakf08rlu37941vmf7e81pcm` (`uuid`),
    CONSTRAINT `FKnh5o5squ94srkbwfbj5geu5xj` FOREIGN KEY (`uuid`) REFERENCES `user` (`uuid`)
);

CREATE TABLE `entry`
(
    `id`               bigint NOT NULL AUTO_INCREMENT,
    `content`          varchar(255) DEFAULT NULL,
    `create_date`      datetime(6)  DEFAULT NULL,
    `eid`              varchar(255) DEFAULT NULL,
    `last_update_date` datetime(6)  DEFAULT NULL,
    `openfcomment`     bit(1) NOT NULL,
    `uuid`             varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_2x58q010y8jt2rvnmgpmwr2or` (`eid`),
    KEY `FK60fly3y0dcwifd9amfiq0uqnr` (`uuid`),
    CONSTRAINT `FK60fly3y0dcwifd9amfiq0uqnr` FOREIGN KEY (`uuid`) REFERENCES `user` (`uuid`)
);

CREATE TABLE `comment`
(
    `id`               bigint NOT NULL AUTO_INCREMENT,
    `cid`              varchar(255) DEFAULT NULL,
    `content`          varchar(255) DEFAULT NULL,
    `create_date`      datetime(6)  DEFAULT NULL,
    `last_update_date` datetime(6)  DEFAULT NULL,
    `eid`              varchar(255) DEFAULT NULL,
    `uuid`             varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_43basoxsi5s56tfcisj1emswv` (`cid`),
    KEY `FKneuo6dny3e1kdh67lprhh4bmd` (`eid`),
    KEY `FKdovegncldraxfqdw7j9jk0pkv` (`uuid`),
    CONSTRAINT `FKdovegncldraxfqdw7j9jk0pkv` FOREIGN KEY (`uuid`) REFERENCES `user` (`uuid`),
    CONSTRAINT `FKneuo6dny3e1kdh67lprhh4bmd` FOREIGN KEY (`eid`) REFERENCES `entry` (`eid`)
);
CREATE TABLE `user_roles`
(
    `auth_id` bigint NOT NULL,
    `id`      bigint NOT NULL,
    PRIMARY KEY (`auth_id`, `id`),
    KEY `FKj84giw7nh1ik1lk91e5t58hf1` (`id`),
    CONSTRAINT `FKj84giw7nh1ik1lk91e5t58hf1` FOREIGN KEY (`id`) REFERENCES `role` (`id`),
    CONSTRAINT `FKjiopguoqgp021wxsymkx3ta54` FOREIGN KEY (`auth_id`) REFERENCES `authorization` (`id`)
);
