ALTER TABLE `task`
    ADD COLUMN `city` VARCHAR(50) NOT NULL DEFAULT 'Kyiv' AFTER `company`;

ALTER TABLE `task`
    CHANGE COLUMN `city` `city` VARCHAR(50) NOT NULL;
