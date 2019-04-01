package com.sdehunt;

import com.sdehunt.repository.JdbiTaskRepository;
import com.sdehunt.repository.TaskRepository;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Connection;

@SpringBootApplication
public class Application {

    @Bean
    public TaskRepository taskRepository() {
            return new JdbiTaskRepository(Util.getDBConnection());
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}