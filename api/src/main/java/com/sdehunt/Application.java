package com.sdehunt;

import com.sdehunt.commons.github.GithubClient;
import com.sdehunt.commons.github.JavaGithubClient;
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
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.sql.DataSource;

@EnableSwagger2
@SpringBootApplication
public class Application implements WebMvcConfigurer {

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
    public TaskRepository taskRepository(DataSource dataSource) {
        return new JdbiTaskRepository(dataSource);
    }

    @Bean
    public SolutionRepository solutionRepository(DataSource dataSource) {
        return new JdbiSolutionRepository(dataSource);
    }

    @Bean
    public UserRepository userRepository(DataSource dataSource) {
        return new JdbiUserRepository(dataSource);
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
    public SolutionService solutionService(
            GeneralScoreCounter scoreCounter,
            SolutionRepository solutionRepository,
            GithubClient githubClient,
            ParameterService params
    ) {
        return new SolutionService(scoreCounter, solutionRepository, githubClient, params);
    }

    @Bean
    public ParameterService params() {
        return new HardCachedParameterService(new SsmParameterService());
    }

    @Bean
    public DataSource dataSource(ParameterService params) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://" + params.get("RDS_HOST") + ":" + params.get("RDS_PORT") + "?useSSL=true&verifyServerCertificate=true");
        dataSource.setUsername(params.get("RDS_USER"));
        dataSource.setPassword(params.get("RDS_PASSWORD"));
        return dataSource;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}