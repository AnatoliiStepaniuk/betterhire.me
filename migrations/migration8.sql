ALTER TABLE `best_solution`
    CHANGE COLUMN `task` `task` ENUM ('slides', 'slides_test', 'pizza', 'cars', 'letters', 'cities', 'letter', 'city') NOT NULL;

ALTER TABLE `solution`
    CHANGE COLUMN `task` `task` ENUM ('slides', 'slides_test', 'pizza', 'cars', 'letters', 'cities', 'letter', 'city') NOT NULL;

ALTER TABLE `solution_repo`
    CHANGE COLUMN `task` `task` ENUM ('slides', 'slides_test', 'cars', 'balloons', 'datacenter', 'pizza', 'letters', 'cities', 'letter', 'city') NOT NULL;

ALTER TABLE `task`
    CHANGE COLUMN `task` `task` ENUM ('slides', 'slides_test', 'cars', 'balloons', 'datacenter', 'pizza', 'letters', 'cities', 'letter', 'city') NOT NULL;

ALTER TABLE `template`
    CHANGE COLUMN `task` `task` ENUM ('slides', 'slides_test', 'pizza', 'cars', 'letters', 'cities', 'letter', 'city') NOT NULL;

INSERT INTO `task` (`task`, `description`, `created`, `name`, `short_description`, `enabled`, `submittable`,
                    `image_url`, `test`, `tags`, `languages`, `job`, `job_url`, `company`, `requirements`)
VALUES ('city',
        '# Cities
Combine cities so that the next one starts with the last letter of the previous, like:
 Pari**s**->**S**tockhol**m**->**M**ilan

   Your score is the **number of letters** of all cities in your list.

### Example:
Let''s say `input.txt` has these cities:
```
London
Milan
Stockholm
Paris
```
Your `output.txt` file can look like this::
```
Paris
Stockholm
Milan
```
Your score for this solution is 19 (total number of letters in all cities).

#### All necessary files are present in your solution repository. Click the button and start coding!',
        '1566671043',
        'Junior Java Developer',
        'Combine cities so that the next one starts with the last letter of the previous',
        '1',
        '1',
        'https://betterhire-tasks.s3.eu-central-1.amazonaws.com/cities/agile.png',
        '0',
        'easy',
        'java',
        'We are looking for Junior/Middle Java developer for the unique IoT-platform. You will be work with real-time big data, data science, predictive analysis and and will develop a product that used by Fortune 500 companies.',
        'https://jobs.dou.ua/companies/agileengine/vacancies/71756/',
        'AgileEngine is a Top-3 rated software development company in Ukraine, Argentina and Washington DC.',
        'requirements');

INSERT INTO `template` (`task`, `language`, `repo`)
VALUES ('city', 'java', 'city_java');

INSERT INTO `task` (`task`, `description`, `created`, `name`, `short_description`, `enabled`, `submittable`,
                    `image_url`, `test`, `tags`, `languages`, `job`, `job_url`, `company`, `requirements`)
VALUES ('letter',
        '# Letters Game

You need to create words from a set of random letters. Each letter can be used at most once. Each word can be used at most once. Your total score is a number of letters in all words you''ve created.

## Input files format

Files come in pairs - with words and with letters.  Named respectivly `a_letters.txt`, `a_words.txt`, `b_letters.txt`, `b_words.txt`, etc.

## Output file format
Letters files should be named `a_result.txt`, `b_result.txt`, etc. and contain words separated by newline.

### Example:
File `a_words.txt` has words:
```
better
hire
me
```
File `a_letters.txt` has letters:
```
i
h
m
b
r
e
t
r
e
```
From these letters you can create 2 words (`hire` and `me`), so the file should have the following content:
```
hire
me
```
Your score for this solution is 6 (total number of letters in all words).

#### All necessary files are present in your solution repository. Click the button and start coding!',
        '1566711495',
        'Junior JavaScript Developer',
        'Arrange letters into words. Pretty simple, huh?',
        '1',
        '1',
        'https://betterhire-tasks.s3.eu-central-1.amazonaws.com/letters/jooble.png',
        '0',
        'easy',
        'js',
        'We are looking for Junior JavaScript developer with SQL knowledge.',
        'https://jobs.dou.ua/companies/jooble/vacancies/100407/',
        'Jooble is a product IT company with 2 millions daily users',
        'requirements');

INSERT INTO `template` (`task`, `language`, `repo`)
VALUES ('letter', 'js', 'letter_js');
