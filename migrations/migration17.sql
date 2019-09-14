CREATE TABLE `task_application`
(
    `id`       BIGINT        NOT NULL AUTO_INCREMENT,
    `task`     VARCHAR(36)   NOT NULL,
    `company`  VARCHAR(50)   NOT NULL,
    `job_url`  VARCHAR(2000) NOT NULL,
    `task_url` VARCHAR(2000) NOT NULL,
    `created`  INT(10)       NOT NULL,
    PRIMARY KEY (`id`)
);
