ALTER TABLE `task`
    ADD COLUMN `job` VARCHAR(2000) NULL AFTER `languages`;

ALTER TABLE `task`
    ADD COLUMN `job_url` VARCHAR(2000) NULL DEFAULT NULL AFTER `job`;

ALTER TABLE `task`
    ADD COLUMN `company` VARCHAR(1000) NULL DEFAULT NULL AFTER `job_url`;

UPDATE `task`
SET `name`      = 'Junior C++ Engineer',
    `languages` = 'cpp',
    `image_url` = 'https://betterhire-tasks.s3.eu-central-1.amazonaws.com/cities/ring.jpg',
    `job`       = 'We are looking for a Junior C++ developer, who will work on firmware development for Ring powered devices (video cameras and doorbells). The SW solution addresses multiple challenges (should work 24/7 under different outdoor conditions, connected over WiFi, performs OTA updates etc.). We have huge amount of work to do, and need all your best skills and experience in writing high-quality code, using different libraries, C++ 11/14 etc.',
    `job_url`   = 'https://jobs.dou.ua/companies/ring-ukraine/vacancies/99273',
    `company`   = 'Ring Ukraine is a product company creating state of the art smart home security systems. We are focused on applying AI, machine learning, computer vision and intellectual data analysis technology for Ring products.'
WHERE (`task` = 'cities' AND `enabled` = true);

UPDATE `task`
SET `name`      = 'Junior Python Developer',
    `languages` = 'python',
    `image_url` = 'https://betterhire-tasks.s3.eu-central-1.amazonaws.com/letters/zeo.jpg',
    `job`       = 'The unique opportunity to contribute to a product that will make revolution in privacy industry by returning a choice of privacy to millions of internet users. You will be proud of every piece of code you create. You will not be bored as Agility is within our DNA.',
    `job_url`   = 'https://jobs.dou.ua/companies/zeo-alliance/vacancies/101036',
    `company`   = 'ZEO Alliance is a technology driven company creating products and experiences that positively impact the world. We are the company where the skills of world-class engineers collide with the creative instincts of entrepreneurs.'
WHERE (`task` = 'letters' AND `enabled` = true);

UPDATE `task`
SET `enabled` = '0'
WHERE (`task` = 'slides');
UPDATE `task`
SET `enabled` = '0'
WHERE (`task` = 'cars');
UPDATE `task`
SET `enabled` = '0'
WHERE (`task` = 'balloons');
UPDATE `task`
SET `enabled` = '0'
WHERE (`task` = 'datacenter');
UPDATE `task`
SET `enabled` = '0'
WHERE (`task` = 'pizza');
