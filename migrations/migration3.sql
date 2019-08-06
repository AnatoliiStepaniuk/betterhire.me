ALTER TABLE `task`
    ADD COLUMN `languages` SET ('js', 'java', 'other') NULL DEFAULT 'other' AFTER `last_submit`;
