package com.realestate.crawler.admin.configuration;

import com.google.common.collect.Lists;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI appOpenAPI() {
        return new OpenAPI().servers(Lists.newArrayList(
                new Server().url("http://localhost:8083")
        ))
                .info(new Info().title("Real Estate Crawler Application API")
                        .description("Sample OpenAPI 3.0")
                        .contact(new Contact()
                                .email("vohoanglong07@gmail.com")
                                .name("Long Vo")
                                .url("https://longvohoang.me/"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html"))
                        .version("1.0.0"));
    }
}
