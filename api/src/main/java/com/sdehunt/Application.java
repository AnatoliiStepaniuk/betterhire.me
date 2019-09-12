package com.sdehunt;

import com.sdehunt.commons.email.EmailSender;
import com.sdehunt.commons.email.SesEmailSender;
import com.sdehunt.commons.file.FileDownloader;
import com.sdehunt.commons.file.UnirestFileDownloader;
import com.sdehunt.commons.github.CachingGithubClient;
import com.sdehunt.commons.github.GithubClient;
import com.sdehunt.commons.github.UnirestGithubClient;
import com.sdehunt.commons.params.EhcacheParameterService;
import com.sdehunt.commons.params.ParameterService;
import com.sdehunt.commons.params.SsmParameterService;
import com.sdehunt.commons.repo.AccessTokenRepository;
import com.sdehunt.commons.repo.JdbiAccessTokenRepository;
import com.sdehunt.repository.*;
import com.sdehunt.repository.cache.CachingSolutionRepository;
import com.sdehunt.repository.cache.CachingUserRepository;
import com.sdehunt.repository.impl.*;
import com.sdehunt.score.GeneralFilesDownloader;
import com.sdehunt.score.GeneralScoreCounter;
import com.sdehunt.security.AppProperties;
import com.sdehunt.service.*;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${RDS_HOST}")
    private String rdsHost;

    @Value("${RDS_PORT}")
    private int rdsPort;

    @Value("${RDS_USER}")
    private String rdsUser;

    @Value("${RDS_PASSWORD}")
    private String rdsPassword;

    @Value("${RDS_DB}")
    private String rdsDb;

    @Value("${GITHUB_LOGIN}")
    private String githubLogin;

    @Value("${GITHUB_ACCESS_TOKEN}")
    private String githubAccessToken;

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
        return new JdbiTaskRepository(dataSource, rdsDb);
    }

    @Bean
    public SolutionRepository solutionRepository(DataSource dataSource) {
        return new CachingSolutionRepository(new JdbiSolutionRepository(dataSource, rdsDb));
    }

    @Bean
    public SolutionRepoRepository solutionRepoRepository(DataSource dataSource) {
        return new JdbiSolutionRepoRepository(dataSource, rdsDb);
    }

    @Bean
    public TemplateRepository templateRepository(DataSource dataSource) {
        return new JdbiTemplateRepository(dataSource, rdsDb);
    }

    @Bean
    public ReviewRepository reviewRepository(DataSource dataSource) {
        return new JdbiReviewRepository(dataSource, rdsDb);
    }

    @Bean
    public UserRepository userRepository(DataSource dataSource) {
        return new CachingUserRepository(new JdbiUserRepository(dataSource, rdsDb));
    }

    @Bean
    public AccessTokenRepository accessTokenRepository(DataSource dataSource) {
        return new JdbiAccessTokenRepository(dataSource, rdsDb);
    }

    @Bean
    public BestSolutionRepository bestSolutionRepository(DataSource dataSource) {
        return new JdbiBestSolutionRepository(dataSource, rdsDb);
    }

    @Bean
    public GithubClient githubClient(AccessTokenRepository accessTokens) {
        return new CachingGithubClient(new UnirestGithubClient(githubAccessToken, accessTokens));
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
    public BestSolutionService bestSolutionService(BestSolutionRepository bestSolutions, UserRepository users, TaskRepository tasks) {
        return new BestSolutionService(bestSolutions, users, tasks);
    }

    @Bean
    public SolutionRepoService solutionRepoService(TemplateRepository templates,
                                                   UserRepository users,
                                                   GithubClient githubClient,
                                                   SolutionRepoRepository solutionRepos) {
        return new SolutionRepoService(githubLogin, templates, users, githubClient, solutionRepos);
    }

    @Bean
    public ProfileNotificationService profileNotificationService(GithubClient githubClient) {
        return new ProfileNotificationService(githubClient);
    }

    @Bean
    public SolutionService solutionService(
            GeneralScoreCounter scoreCounter,
            TaskRepository taskRepository,
            SolutionRepository solutionRepository,
            UserRepository userRepository,
            GithubClient githubClient,
            ParameterService params,
            BestSolutionService bestSolutionService,
            ProfileNotificationService profileNotificationService
    ) {
        return new SolutionService(scoreCounter, taskRepository, solutionRepository, userRepository, githubClient, params, bestSolutionService, profileNotificationService);
    }

    @Bean
    public CvService cvService() {
        return new CvService();
    }

    @Bean
    public EmailSender emailSender() {
        return new SesEmailSender();
    }

    @Bean
    public EmailService emailService(UserRepository userRepository, EmailSender emailSender) {
        return new EmailService(userRepository, emailSender);
    }

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://" + rdsHost + ":" + rdsPort + "?useSSL=true&verifyServerCertificate=true&useUnicode=true&characterEncoding=UTF-8");
        dataSource.setUsername(rdsUser);
        dataSource.setPassword(rdsPassword);
        return dataSource;
    }

    @Bean
    public ExtendedUserService extendedUserService(UserRepository usersRepo,
                                                   ReviewRepository reviewsRepo,
                                                   SolutionRepository solutionsRepo,
                                                   TaskRepository tasksRepo) {
        return new ExtendedUserService(
                usersRepo,
                reviewsRepo,
                solutionsRepo,
                tasksRepo
        );
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