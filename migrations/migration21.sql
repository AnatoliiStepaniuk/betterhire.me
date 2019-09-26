INSERT INTO `task` (`type`, `task`, `description`, `created`, `name`, `short_description`, `enabled`, `submittable`,
                    `image_url`, `test`, `participants`, `offers`, `bestOffer`, `requirements`, `input`, `tags`,
                    `users`, `languages`, `job`, `job_url`, `company`, `city`, `emails`)
VALUES ('manual', 'backendless1',
        '# Java Developer for SaaS platform\n\n### OVERVIEW\n\nDevelop a basic TODO-app in Java using the Backendless APIs and the backend storage.\n\n### GOALS\n\n1. Demonstrate your knowledge of the Java programming language;\n2. Demonstrate your understanding of third-party API usage;\n3. Deliver a complete application with instructions for how to run it;\n4. Use your architectural and engineering skills to make the app extensible and easy to\n    understand;\n5. Show your skills in using Git and Maven/Gradle effectively;\n\n### SPECIFICATIONS\n\n#### Functionality\n\nThe basic TODO-app is a CRUD application with the following functional requirements:\n\n1. Create and save a TODO item\n2. Read all TODO items\n3. Update the selected TODO item\n4. Delete (mark “completed”) a TODO item\nAdditional points for setting up an automatic cleanup of the “completed” TODO items after some\nconfigurable time period.\n\n#### Technical requirements\n\n\nThe TODO-app is purely a client-side app, no backend should be written for that. As a backend\nstorage, use Backendless Data Service. You can use either Backendless SDK for Android/Java or\nBackendless REST API. Here is a relevant documentation page:\n\nJava/Android SDK: ​http://backendless.com/docs/android\n\nREST API: ​http://backendless.com/docs/rest\n\nIf you decide to implement with the REST API, you are free to use any HTTP library or\nalternatively implement the HTTP calls by the means of JDK. The goal is the conciseness and\nreadability of the code, not the final jar size (though of course putting the whole Spring\nFramework here would be an overkill).\nUse public Git (e.g. GitHub) and develop the app iteratively with commits describing the steps. It\nwould be a plus if the git history nicely showed your development and thinking process.\nUse Maven or Gradle to build your project. Include the instructions in the README.md file.\nKeep in mind that the design of your app should be open to changes. For example, the further\nrequirements may include sharing a todo item (sending it to someone), setting the priorities to the\nitems and sorting them in the list accordingly, setting the deadline for the item and so on. So your\ndesign should be simple and assume as little constraints as possible. The best way is to consider\nthe project as an MVP ​(minimum viable product)​, which may and will change in unexpected ways,\nbut with the exception that the code should be as clean as possible.\nIf you decide to implement automatic cleanup of the completed items, the ​Timers​ functionality is\nrecommended to use.\n\n#### User Interface\n\nYou are not required to write any sophisticated user interface implementation. The TODO app\nitself is best written in a library style, so that it is open for any interface implementation anyone\nwould write. So for the completeness of the assignment it is sufficient to have only a convenient\ninterface and a main() method demonstrating the usage.\nThough preparing a clean command line interface or even implementing an Android app on top\nof that would be beneficial, it won’t be considered much in the decision process. Moreover, a bad\nimplementation of the interface can show you from the negative side, so implement it only if\nyou’re confident in your skills.\n\n#### Submission\n\n\nThe app is expected to be built with either Maven or Gradle. The instructions on how to build and\nrun should be included in the README.md file. You are also welcome to attach a cover letter\ndescribing any parts of your code, your thoughts on the choices you had to make, and any\ndescription you would like to provide additionally.',
        '1569502184', 'Java Developer for SaaS platform',
        'Product company with modern technology stack serving tens of thousands of users from all over the world', '1',
        '1', 'https://betterhire-tasks.s3.eu-central-1.amazonaws.com/backendless1/backendless.png', '0', '0', '0', '0',
        'requirements', null, 'hot', '0', 'java',
        'If you are looking for a product company with a product based on a modern technology stack serving tens of thousands of users from all over the world and want to contribute to the development of an advanced platform, then pay attention to this vacancy.

        Backendless is expanding its staff and is looking for a Java programmer. Requirements include: practical knowledge of Java software development. You must be able to demonstrate knowledge of the programming language, have an analytical mindset, the ability to solve non-trivial tasks using interesting, algorithmically and architecturally-competent approaches. We highly value and also require knowledge of object-oriented programming.

        The technology stack includes: Docker, Nginx, Redis, Play Framework, JPA, MongoDB, MySQL, NodeJS.

        Requirements:
        - Experience in developing Java applications (server technology) (1+ years);
        - Knowledge, understanding and experience in the practical application of object-oriented programming;
        - Knowledge of multithreading, as well as the network API in Java;
        - Experience with databases, knowledge of SQL, JDBC;
        - Ability to quickly write concise, understandable, high-quality and elegant code;

        We give preference to candidates who:
        - Have extensive knowledge and experience in various fields of computer technology and mathematics
        - Participate (or participated) in Olympiads, write articles for the industry, blog
        - Passionate about their work and live for her
        - Prefer to work in a highly energetic environment in small teams of the most talented people',
        '', 'backendless', 'Kyiv', 'anatolii.stepaniuk@gmail.com');

INSERT INTO `template` (`language`, `repo`, `task`)
VALUES ('java', 'backendless1', 'backendless1');
