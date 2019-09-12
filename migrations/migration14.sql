UPDATE `task`
SET company = 'practice';

ALTER TABLE `task`
    CHANGE COLUMN `company` `company` VARCHAR(1000) NOT NULL;

UPDATE `task`
SET company = 'intersog'
WHERE task = 'intersog1';

