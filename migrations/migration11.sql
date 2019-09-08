ALTER TABLE `task`
    ADD COLUMN `type` ENUM ('AUTO', 'MANUAL') NOT NULL DEFAULT 'AUTO' AFTER `id`;

ALTER TABLE `task`
    CHANGE COLUMN `task` `task` ENUM ('slides', 'slides_test', 'cars', 'balloons', 'datacenter', 'pizza', 'letters', 'cities', 'letter', 'city', 'intersog1') NOT NULL;

ALTER TABLE `task`
    CHANGE COLUMN `requirements` `requirements` VARCHAR(2000) NULL;

ALTER TABLE `task`
    CHANGE COLUMN `description` `description` TEXT CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NULL DEFAULT NULL;

INSERT INTO `task` (`type`, `task`, `description`, `created`, `name`, `input`, `languages`, `job`, `job_url`, `company`,
                    `image_url`)
VALUES ('manual', 'intersog1',
        '# Intersog: Junior Python Developer task

Create an application for monitoring file system events in a certain directory.

The app has to read config (e.g. `config.ini`, `settings.py`) and setup configurations.
The application must detect events that occur in the file system. For example file or folder was created/deleted/changed.
When event occurs -> certain Handler (registered in config) should be used to process that event.
It''s up to you which handlers to implement. It can be anything (compiling/renaming/deleting...).
Map handlers and patterns in the config file.

##### Example:
`Workdir:`
- `<filename1>.<file_extention_to_delete>` -> delete it
- `<filename2>.c` -> compile it
- `<filename3>.jpg` -> convert to .png
- `<filename4>.doc` -> to .pdf

##### Requirements:
- application must not use all CPU time of one or more cores.
- implement a minimum of 3 different event handlers.
- possibility to add or remove handlers.

##### Not required but will be a very big plus:
1. Daemonization of app process. +
1. Concurrency. ++
1. Docker containerization. ++
1. Implement a module for notifying the user about events that have occured. +++
1. Readme.md ++++

### To submit your solution just push your code to this repo 😎

Happy coding! 😉
',
        '1567852033',
        'Junior Python Developer (Kyiv)',
        'https://github.com/BetterHireMe/intersog1',
        'python',
        'Intersog® is a Chicago-based provider of ROI-driven custom web and mobile development specializing in delivery of end-to-end solutions to Fortune 500 companies, SMEs and startups.

We’re looking for an **Intern/Junior Python developer** to join the team of our Israeli client that provides advertising services based on personalized recommendations and data analysis.

#### Requirements
- Experience with Django and Django Rest Framework;
- Basic knowledge of SQL and RDBMS (PostgreSQL, MySQL);
- Experience with asyncio;
- At least intermediate English (both written and spoken).

#### Bonus if you have
- Experience with aiohttp;
- Experience with unit testing;
- Experience with Docker;
- Experience with AWS infrastructure;
- Understanding on NoSQL databases (MongoDB, DynamoDB).
',
        'https://careers.intersog.com/job/intern-junior-python-developer',
        'Intersog® is a Chicago-based provider of ROI-driven custom web and mobile development specializing in delivery of end-to-end solutions to Fortune 500 companies, SMEs and startups.',
        'https://betterhire-tasks.s3.eu-central-1.amazonaws.com/intersog1/intersog.png');

ALTER TABLE `solution`
    CHANGE COLUMN `status` `status` ENUM ('accepted', 'in_progress', 'invalid_files', 'invalid_solution', 'timeout', 'error', 'waiting_for_review', 'reviewed') NOT NULL DEFAULT 'in_progress';

ALTER TABLE `template`
    CHANGE COLUMN `task` `task` ENUM ('slides', 'slides_test', 'pizza', 'cars', 'letters', 'cities', 'letter', 'city', 'intersog1') NOT NULL;

INSERT INTO `template` (`task`, `language`, `repo`)
VALUES ('intersog1', 'python', 'intersog1');

ALTER TABLE `best_solution`
    CHANGE COLUMN `task` `task` ENUM ('slides', 'slides_test', 'pizza', 'cars', 'letters', 'cities', 'letter', 'city', 'intersog1') NOT NULL;

ALTER TABLE `solution`
    CHANGE COLUMN `task` `task` ENUM ('slides', 'slides_test', 'pizza', 'cars', 'letters', 'cities', 'letter', 'city', 'intersog1') NOT NULL;

ALTER TABLE `solution_repo`
    CHANGE COLUMN `task` `task` ENUM ('slides', 'slides_test', 'cars', 'balloons', 'datacenter', 'pizza', 'letters', 'cities', 'letter', 'city', 'intersog1') NOT NULL;
