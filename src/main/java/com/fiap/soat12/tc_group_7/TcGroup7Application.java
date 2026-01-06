package com.fiap.soat12.tc_group_7;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class TcGroup7Application {

	public static void main(String[] args) {
		SpringApplication.run(TcGroup7Application.class, args);
	}

    @Bean
    ApplicationRunner applicationRunner(@Value("${SPRING_DATASOURCE_USERNAME}") String secret) {
        return args -> {
            System.out.println("SECRET FROM AWS LOADED: " + secret);
        };
    }
}
