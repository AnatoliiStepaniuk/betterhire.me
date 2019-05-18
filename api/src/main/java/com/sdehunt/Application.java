package com.sdehunt;

import com.sdehunt.commons.GithubClient;
import com.sdehunt.commons.JavaGithubClient;
import com.sdehunt.commons.params.HardCachedParameterService;
import com.sdehunt.commons.params.ParameterService;
import com.sdehunt.commons.params.SsmParameterService;
import com.sdehunt.repository.SolutionRepository;
import com.sdehunt.repository.TaskRepository;
import com.sdehunt.repository.UserRepository;
import com.sdehunt.repository.impl.JdbiSolutionRepository;
import com.sdehunt.repository.impl.JdbiTaskRepository;
import com.sdehunt.repository.impl.JdbiUserRepository;
import com.sdehunt.score.GeneralScoreCounter;
import com.sdehunt.service.solution.SolutionService;
import com.sdehunt.util.ConnectionHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.sql.Connection;

@EnableSwagger2
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public ParameterService parameterService() {
        return new HardCachedParameterService(new SsmParameterService());
    }

    @Bean
    public Connection connection() {
        return ConnectionHelper.getDBConnection();
    }

    @Bean
    public TaskRepository taskRepository(Connection connection) {
        return new JdbiTaskRepository(connection);
    }

    @Bean
    public SolutionRepository solutionRepository(Connection connection) {
        return new JdbiSolutionRepository(connection);
    }

    @Bean
    public UserRepository userRepository(Connection connection) {
        return new JdbiUserRepository(connection);
    }


    @Bean
    public GithubClient githubClient() {
        return new JavaGithubClient();
    }

    @Bean
    public GeneralScoreCounter generalScoreCounter(GithubClient githubClient) {
        return new GeneralScoreCounter(githubClient);
    }

    @Bean
    public SolutionService solutionService(GeneralScoreCounter scoreCounter, SolutionRepository solutionRepository){
        return new SolutionService(scoreCounter, solutionRepository);
    }
}