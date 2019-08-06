ALTER TABLE `task`
    ADD COLUMN `languages` SET ('js', 'java', 'other') NULL DEFAULT 'other' AFTER `last_submit`;

ALTER TABLE `template`
    ADD COLUMN `language` ENUM ('js', 'java', 'other') NOT NULL DEFAULT 'other' AFTER `task`;

ALTER TABLE `template`
    DROP PRIMARY KEY,
    ADD PRIMARY KEY (`task`, `language`);
;
