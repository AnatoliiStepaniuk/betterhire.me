ALTER TABLE `user`
    ADD COLUMN `city` VARCHAR(50) NULL DEFAULT NULL AFTER `image_url`,
    ADD COLUMN `languages` SET ('js', 'java', 'python', 'cpp', 'other') NULL AFTER `city`;
