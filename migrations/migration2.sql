CREATE TABLE `solution_repo`
(
    `id`             BIGINT                                                                               NOT NULL AUTO_INCREMENT,
    `task`           ENUM ('slides', 'slides_test', 'cars', 'balloons', 'datacenter', 'pizza', 'letters') NOT NULL,
    `user`           VARCHAR(36)                                                                          NOT NULL,
    `repo`           VARCHAR(140)                                                                         NOT NULL,
    `webhook_secret` VARCHAR(200)                                                                         NULL,
    `created`        INT(10)                                                                              NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `template`
(
    `task` ENUM ('slides', 'slides_test', 'pizza', 'cars', 'letters') NOT NULL,
    `repo` VARCHAR(140)                                               NOT NULL,
    PRIMARY KEY (`task`)
);
