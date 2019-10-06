CREATE TABLE `invitation`
(
    `inviter` VARCHAR(39) NOT NULL,
    `invitee` VARCHAR(39) NOT NULL,
    `when`    INT(10)     NOT NULL,
    `terms`   INT(10)     NOT NULL,
    PRIMARY KEY (`inviter`)
);

ALTER TABLE `invitation`
    ADD COLUMN `status` ENUM ('accepted', 'declined') NOT NULL DEFAULT 'pending' AFTER `terms`;


CREATE TABLE `offer`
(
    `user`     VARCHAR(36)                              NOT NULL,
    `company`  VARCHAR(50)                              NOT NULL,
    `when`     INT(10)                                  NOT NULL,
    `status`   ENUM ('pending', 'accepted', 'declined') NOT NULL,
    `offer`    INT(10)                                  NOT NULL,
    `reporter` VARCHAR(39)                              NOT NULL,
    PRIMARY KEY (`user`)
);

ALTER TABLE `offer`
    ADD COLUMN `offer_currency` VARCHAR(3) NOT NULL AFTER `offer_amount`,
    CHANGE COLUMN `offer` `offer_amount` INT(10) NOT NULL;


CREATE TABLE `contract`
(
    `company`  VARCHAR(50)   NOT NULL,
    `date`     INT(10)       NOT NULL,
    `terms`    INT(10)       NOT NULL,
    `contract` VARCHAR(2000) NULL,
    PRIMARY KEY (`company`)
);


CREATE TABLE `interest`
(
    `user`    VARCHAR(39)                                                       NOT NULL,
    `manager` VARCHAR(39)                                                       NOT NULL,
    `company` VARCHAR(50)                                                       NOT NULL,
    `action`  ENUM ('view_solution', 'view_cv', 'view_github', 'view_linkedin') NOT NULL,
    `date`    INT(10)                                                           NOT NULL,
    PRIMARY KEY (`user`)
);


CREATE TABLE `bonus_invitation`
(
    `inviter`        VARCHAR(39)                                     NOT NULL,
    `invitee`        VARCHAR(39)                                     NOT NULL,
    `created`        INT(10)                                         NOT NULL,
    `bonus_amount`   INT(10)                                         NOT NULL,
    `bonus_currency` VARCHAR(3)                                      NOT NULL,
    `status`         ENUM ('company_pending', 'pending', 'paid_out') NOT NULL,
    PRIMARY KEY (`inviter`)
);

ALTER TABLE `bonus_invitation`
    ADD COLUMN `enabled` TINYINT(1) NOT NULL DEFAULT 1 AFTER `status`,
    ADD COLUMN `comment` VARCHAR(2000) NULL AFTER `enabled`,
    CHANGE COLUMN `status` `status` ENUM ('company_pending', 'pending', 'paid_out', 'not_eligible') NOT NULL;


CREATE TABLE `bonus_company`
(
    `company`        VARCHAR(50)                                        NOT NULL,
    `user`           VARCHAR(39)                                        NOT NULL,
    `bonus_amount`   INT(10)                                            NOT NULL,
    `bonus_currency` VARCHAR(3)                                         NOT NULL,
    `created`        INT(10)                                            NOT NULL,
    `status`         ENUM ('pending', 'paid', 'not_eligible', 'denial') NOT NULL,
    `enabled`        TINYINT(1)                                         NOT NULL DEFAULT 1,
    `comment`        VARCHAR(2000)                                      NULL,
    PRIMARY KEY (`company`)
);
