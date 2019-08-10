ALTER TABLE `task`
    CHANGE COLUMN `task` `task` ENUM ('slides', 'slides_test', 'cars', 'balloons', 'datacenter', 'pizza', 'letters', 'cities') NOT NULL;

ALTER TABLE `best_solution`
    CHANGE COLUMN `task` `task` ENUM ('slides', 'slides_test', 'pizza', 'cars', 'letters', 'cities') NOT NULL;

ALTER TABLE `solution`
    CHANGE COLUMN `task` `task` ENUM ('slides', 'slides_test', 'pizza', 'cars', 'letters', 'cities') NOT NULL;

ALTER TABLE `solution_repo`
    CHANGE COLUMN `task` `task` ENUM ('slides', 'slides_test', 'cars', 'balloons', 'datacenter', 'pizza', 'letters', 'cities') NOT NULL;

ALTER TABLE `template`
    CHANGE COLUMN `task` `task` ENUM ('slides', 'slides_test', 'pizza', 'cars', 'letters', 'cities') NOT NULL;

INSERT INTO `task` (`task`, `description`, `created`, `name`, `short_description`, `enabled`, `submittable`,
                    `image_url`, `test`, `participants`, `offers`, `bestOffer`, `requirements`, `input`, `tags`,
                    `users`, `languages`)
VALUES ('cities', 'description', '1565423587', 'Cities', 'Short description', '1', '1',
        'https://betterhire-tasks.s3.eu-central-1.amazonaws.com/cities/cities.jpg', '0', '0', '0', '0', 'requirements',
        'https://github.com/BetterHireMe/cities_solution', 'easy', '0', 'js,java,other');

INSERT INTO `template` (`task`, `language`, `repo`)
VALUES ('cities', 'other', 'cities');
INSERT INTO `template` (`task`, `language`, `repo`)
VALUES ('cities', 'js', 'cities_js');
INSERT INTO `template` (`task`, `language`, `repo`)
VALUES ('cities', 'java', 'cities_java');
