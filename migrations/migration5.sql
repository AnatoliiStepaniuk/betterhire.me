ALTER TABLE `template`
    CHANGE COLUMN `language` `language` ENUM ('js', 'java', 'other', 'python', 'cpp') NOT NULL DEFAULT 'other';

INSERT INTO `template` (`task`, `language`, `repo`)
VALUES ('letters', 'python', 'letters_solution_python');
INSERT INTO `template` (`task`, `language`, `repo`)
VALUES ('cities', 'python', 'cities_python');

ALTER TABLE `solution_repo`
    CHANGE COLUMN `language` `language` ENUM ('js', 'java', 'python', 'cpp', 'other') NOT NULL DEFAULT 'other';

ALTER TABLE `task`
    CHANGE COLUMN `languages` `languages` SET ('js', 'java', 'python', 'cpp', 'other') NULL DEFAULT 'other';

UPDATE `task`
SET `languages` = 'js,java,other,python'
WHERE (`task` = 'cities' AND `enabled` = true);
UPDATE `task`
SET `languages` = 'js,java,other,python'
WHERE (`task` = 'letters' AND `enabled` = true);

