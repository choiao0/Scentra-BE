package com.apollo.scentraapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ScentraApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScentraApiApplication.class, args);
    }

}
