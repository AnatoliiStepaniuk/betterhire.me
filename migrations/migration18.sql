ALTER TABLE `best_solution`
    ADD COLUMN `task_id` VARCHAR(36) NULL AFTER `solution`;

UPDATE best_solution
SET task_id = task;

ALTER TABLE `best_solution`
    CHANGE COLUMN `task_id` `task_id` VARCHAR(36) NOT NULL ,
    DROP PRIMARY KEY,
    ADD PRIMARY KEY (`user`, `task_id`);

ALTER TABLE `best_solution`
    DROP COLUMN `task`;

ALTER TABLE `best_solution`
    CHANGE COLUMN `task_id` `task` VARCHAR(36) NOT NULL;



ALTER TABLE `template`
    ADD COLUMN `task_id` VARCHAR(36) NULL AFTER `repo`;

UPDATE template
SET task_id = task;

ALTER TABLE `template`
    CHANGE COLUMN `task_id` `task_id` VARCHAR(36) NOT NULL ,
    DROP PRIMARY KEY,
    ADD PRIMARY KEY (`task_id`, `language`);

ALTER TABLE `template`
    DROP COLUMN `task`;

ALTER TABLE `template`
    CHANGE COLUMN `task_id` `task` VARCHAR(36) NOT NULL;



ALTER TABLE `solution`
    CHANGE COLUMN `task` `task` VARCHAR(36) NOT NULL;

ALTER TABLE `solution_repo`
    CHANGE COLUMN `task` `task` VARCHAR(36) NOT NULL;



ALTER TABLE `task`
    CHANGE COLUMN `task` `task` VARCHAR(36) NOT NULL;

