CREATE TABLE `solution_review`
(
    `id`       VARCHAR(36)   NOT NULL,
    `user`     VARCHAR(36)   NOT NULL,
    `task`     VARCHAR(36)   NOT NULL,
    `solution` VARCHAR(36)   NOT NULL,
    `grade`    BIGINT(20)    NULL,
    `comment`  VARCHAR(1000) NULL,
    `emoji`    VARCHAR(10)   NULL,
    `reviewer` VARCHAR(36)   NOT NULL,
    `created`  VARCHAR(10)   NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `userIdx` (`user` ASC)
);
