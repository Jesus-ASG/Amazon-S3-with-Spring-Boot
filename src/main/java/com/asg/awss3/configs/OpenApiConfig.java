package com.asg.awss3.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Cloud files with Amazon S3",
                version = "1.0.0",
                description = "Methods for managing files with Amazon S3"
        )
)
public class OpenApiConfig {
}
