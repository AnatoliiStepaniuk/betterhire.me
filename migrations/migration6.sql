INSERT INTO `template` (`task`, `language`, `repo`)
VALUES ('cities', 'cpp', 'cities_cpp');
INSERT INTO `template` (`task`, `language`, `repo`)
VALUES ('letters', 'cpp', 'letters_solution_cpp');

UPDATE `task`
SET `languages` = 'js,java,python,other,cpp'
WHERE (`task` = 'cities' AND `enabled` = true);

UPDATE `task`
SET `languages` = 'js,java,python,other,cpp'
WHERE (`task` = 'letters' AND `enabled` = true);
