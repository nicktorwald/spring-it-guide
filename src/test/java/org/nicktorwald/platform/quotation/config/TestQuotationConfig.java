package org.nicktorwald.platform.quotation.config;

import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.boot.autoconfigure.r2dbc.ConnectionFactoryOptionsBuilderCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Configures an additional persistent components.
 */
@TestConfiguration
public class TestQuotationConfig {

    @Bean
    public PostgreSQLContainer<?> postgresContainer() {
        final PostgreSQLContainer<?> postgresContainer =
                new PostgreSQLContainer<>("postgres:10.6")
                        .withExposedPorts(5432)
                        .withDatabaseName("quotation")
                        .withUsername("quser")
                        .withPassword("pa$$vv0rd");
        postgresContainer.start();
        return postgresContainer;
    }

    @Bean
    @DependsOn("postgresContainer")
    public ConnectionFactoryOptionsBuilderCustomizer connectionFactoryPortCustomizer(PostgreSQLContainer<?> container) {
        var host = container.getHost();
        var port = container.getMappedPort(5432);
        var databaseName = container.getDatabaseName();
        var username = container.getUsername();
        var password = container.getPassword();
        return (builder) -> {
            builder
                    .option(ConnectionFactoryOptions.HOST, host)
                    .option(ConnectionFactoryOptions.PORT, port)
                    .option(ConnectionFactoryOptions.DATABASE, databaseName)
                    .option(ConnectionFactoryOptions.USER, username)
                    .option(ConnectionFactoryOptions.PASSWORD, password);
        };
    }

    @Bean
    @DependsOn("postgresContainer")
    public FlywayConfigurationCustomizer flywayConfigurationCustomizer(PostgreSQLContainer<?> container) {
        var jdbcUrl = container.getJdbcUrl();
        var username = container.getUsername();
        var password = container.getPassword();
        return (configuration) -> {
            configuration.dataSource(jdbcUrl, username, password);
        };
    }

}
