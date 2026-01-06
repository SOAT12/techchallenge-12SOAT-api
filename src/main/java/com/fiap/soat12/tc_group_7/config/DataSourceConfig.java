package com.fiap.soat12.tc_group_7.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Log4j2
@Configuration
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
public class DataSourceConfig {

    @Value("${aws.access.key}")
    private String awsAccessKeyId;

    @Value("${aws.secret.key}")
    private String awsSecretAccessKey;

    @Value("${aws.region}")
    private String awsSecretsManagerRegion;

    @Value("${aws.secretname}")
    private String awsSecretsManagerSecretName;

    @Value("${app.ambiente}")
    private String ambiente;

    @Bean
    public DataSource getDataSource() throws Exception {
        String driverClassName;
        String url;
        String username;
        String password;

        if ("prod".equalsIgnoreCase(ambiente)) {
            SecretsManager.getInstance().builder(
                    awsAccessKeyId,
                    awsSecretAccessKey,
                    awsSecretsManagerRegion,
                    awsSecretsManagerSecretName
            );

            url = SecretsManager.getInstance().get("jdbc_url");
            username = SecretsManager.getInstance().get("username");
            password = SecretsManager.getInstance().get("password");
        } else {
            url = "jdbc:postgresql://localhost:5432/postgres";
            username = "postgres";
            password = "password";
        }

        driverClassName = "org.postgresql.Driver";

        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(driverClassName);
        dataSourceBuilder.url(url);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);

        DataSource dataSource = dataSourceBuilder.build();

        log.info("Database connection valid = {}", dataSource.getConnection().isValid(1000));

        return dataSource;
    }
}
