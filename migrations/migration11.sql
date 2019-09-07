ALTER TABLE `task`
    ADD COLUMN `type` ENUM ('AUTO', 'MANUAL') NOT NULL DEFAULT 'AUTO' AFTER `id`;

ALTER TABLE `task`
    CHANGE COLUMN `task` `task` ENUM ('slides', 'slides_test', 'cars', 'balloons', 'datacenter', 'pizza', 'letters', 'cities', 'letter', 'city', 'intersog1') NOT NULL;

ALTER TABLE `task`
    CHANGE COLUMN `requirements` `requirements` VARCHAR(2000) NULL;

INSERT INTO `task` (`type`, `task`, `description`, `created`, `name`, `input`, `languages`, `job`, `job_url`, `company`,
                    `image_url`)
VALUES ('manual', 'intersog1',
        'Create an application for monitoring file system events in a certain directory. \n\nThe app has to read config (e.g. `config.ini`, `settings.py`) and setup configurations.\nThe application must detect events that occur in the file system. For example file or folder was created/deleted/changed.\nWhen event occurs -> certain Handler (registered in config) should be used to process that event. \nIt\'s up to you which handlers to implement. It can be anything (compiling/renaming/deleting...).\nMap handlers and patterns in the config file. \n\n##### Example: \n`Workdir`\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`\\-` `<filename1>.<file_extention_to_delete>` -> delete it\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; `\\-` `<filename2>.c` -> compile it\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; `\\-` `<filename3>.jpg` -> convert to .png\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; `\\-` `<filename4>.doc` -> to .pdf\n \n##### Requirements: \n- application must not use all CPU time of one or more cores.\n- implement a minimum of 3 different event handlers. \n- possibility to add or remove handlers.\n \n##### Not required but will be a very big plus: \n1. daemonization of app process. +\n1. concurrency. ++ \n1. docker containerization. ++ \n1. implement a module for notifying the user about events that have occured. +++ \n1. Readme.md ++++ \n\n',
        '1567852033', 'Intersog: Junior Python Developer', 'https://github.com/BetterHireMe/intersog1', 'python',
        'We’re looking for an Intern/Junior Python developer to join the team of our Israeli client that provides advertising services based on personalized recommendations and data analysis.',
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
