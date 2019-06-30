package com.sdehunt;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Profile(value = {"dev"})
@Configuration
@EnableSwagger2
public class SwaggerConfig {
}
