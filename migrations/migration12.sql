ALTER TABLE `user`
    ADD COLUMN `available` TINYINT(4) NULL DEFAULT 1 AFTER `last_submit`;
