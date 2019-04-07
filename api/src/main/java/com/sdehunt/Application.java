package com.sdehunt;

import com.sdehunt.repository.SolutionRepository;
import com.sdehunt.repository.TaskRepository;
import com.sdehunt.repository.impl.JdbiSolutionRepository;
import com.sdehunt.repository.impl.JdbiTaskRepository;
import com.sdehunt.util.ConnectionHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public TaskRepository taskRepository() {
            return new JdbiTaskRepository(ConnectionHelper.getDBConnection());
    }

    @Bean
    public SolutionRepository solutionRepository() {
        return new JdbiSolutionRepository(ConnectionHelper.getDBConnection());
    }
}