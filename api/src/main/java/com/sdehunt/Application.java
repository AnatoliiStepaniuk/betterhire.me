package com.sdehunt;

import com.sdehunt.commons.file.FileDownloader;
import com.sdehunt.commons.file.UnirestFileDownloader;
import com.sdehunt.commons.github.GithubClient;
import com.sdehunt.commons.github.UnirestGithubClient;
import com.sdehunt.commons.params.EhcacheParameterService;
import com.sdehunt.commons.params.ParameterService;
import com.sdehunt.commons.params.SsmParameterService;
import com.sdehunt.commons.repo.AccessTokenRepository;
import com.sdehunt.commons.repo.JdbiAccessTokenRepository;
import com.sdehunt.repository.BestSolutionRepository;
import com.sdehunt.repository.SolutionRepository;
import com.sdehunt.repository.TaskRepository;
import com.sdehunt.repository.UserRepository;
import com.sdehunt.repository.impl.JdbiBestSolutionRepository;
import com.sdehunt.repository.impl.JdbiSolutionRepository;
import com.sdehunt.repository.impl.JdbiTaskRepository;
import com.sdehunt.repository.impl.JdbiUserRepository;
import com.sdehunt.score.GeneralFilesDownloader;
import com.sdehunt.score.GeneralScoreCounter;
import com.sdehunt.security.AppProperties;
import com.sdehunt.service.BestSolutionService;
import com.sdehunt.service.SolutionService;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.sql.DataSource;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
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
        return new EhcacheParameterService(new SsmParameterService());
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
    public AccessTokenRepository accessTokenRepository(DataSource dataSource) {
        return new JdbiAccessTokenRepository(dataSource);
    }

    @Bean
    public BestSolutionRepository bestSolutionRepository(DataSource dataSource) {
        return new JdbiBestSolutionRepository(dataSource);
    }

    @Bean
    public GithubClient githubClient(ParameterService params, AccessTokenRepository accessTokens) {
        return new UnirestGithubClient(params, accessTokens);
    }

    @Bean
    public FileDownloader fileDownloader() {
        return new UnirestFileDownloader();
    }

    @Bean
    public GeneralFilesDownloader filesDownloader(GithubClient githubClient, FileDownloader fileDownloader) {
        return new GeneralFilesDownloader(githubClient, fileDownloader);
    }

    @Bean
    public GeneralScoreCounter generalScoreCounter(GeneralFilesDownloader filesDownloader) {
        return new GeneralScoreCounter(filesDownloader);
    }

    @Bean
    public BestSolutionService bestSolutionService(BestSolutionRepository bestSolutions, UserRepository users) {
        return new BestSolutionService(bestSolutions, users);
    }

    @Bean
    public SolutionService solutionService(
            GeneralScoreCounter scoreCounter,
            SolutionRepository solutionRepository,
            UserRepository userRepository,
            GithubClient githubClient,
            ParameterService params,
            BestSolutionService bestSolutionService
    ) {
        return new SolutionService(scoreCounter, solutionRepository, userRepository, githubClient, params, bestSolutionService);
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