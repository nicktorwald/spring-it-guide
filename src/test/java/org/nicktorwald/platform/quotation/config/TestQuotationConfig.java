package org.nicktorwald.platform.quotation.config;

import java.io.IOException;

import de.flapdoodle.embed.process.runtime.Network;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.boot.autoconfigure.r2dbc.ConnectionFactoryOptionsBuilderCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import ru.yandex.qatools.embed.postgresql.PostgresExecutable;
import ru.yandex.qatools.embed.postgresql.PostgresProcess;
import ru.yandex.qatools.embed.postgresql.PostgresStarter;
import ru.yandex.qatools.embed.postgresql.config.AbstractPostgresConfig;
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig;
import ru.yandex.qatools.embed.postgresql.distribution.Version;

/**
 * Configures an additional persistent components.
 */
@TestConfiguration
public class TestQuotationConfig {

    @Bean(destroyMethod = "stop")
    public PostgresProcess postgresProcess(PostgresConfig config) throws IOException {
        PostgresStarter<PostgresExecutable, PostgresProcess> runtime = PostgresStarter.getDefaultInstance();
        PostgresExecutable exec = runtime.prepare(config);
        return exec.start();
    }

    @Bean
    public PostgresConfig postgresConfig() throws IOException {
        return new PostgresConfig(
                Version.V10_6,
                new AbstractPostgresConfig.Net("localhost", Network.getFreeServerPort()),
                new AbstractPostgresConfig.Storage("quotation"),
                new AbstractPostgresConfig.Timeout(),
                new AbstractPostgresConfig.Credentials("quser", "pa$$vv0rd")
        );
    }

    @Bean
    @DependsOn("postgresProcess")
    public ConnectionFactoryOptionsBuilderCustomizer connectionFactoryPortCustomizer(PostgresConfig config) {
        var network = config.net();
        var storage = config.storage();
        var credentials = config.credentials();
        return (builder) -> {
            builder
                    .option(ConnectionFactoryOptions.HOST, network.host())
                    .option(ConnectionFactoryOptions.PORT, network.port())
                    .option(ConnectionFactoryOptions.DATABASE, storage.dbName())
                    .option(ConnectionFactoryOptions.USER, credentials.username())
                    .option(ConnectionFactoryOptions.PASSWORD, credentials.password());
        };
    }

    @Bean
    @DependsOn("postgresProcess")
    public FlywayConfigurationCustomizer flywayConfigurationCustomizer(PostgresConfig config) {
        var network = config.net();
        var credentials = config.credentials();
        var storage = config.storage();
        return (configuration) -> {
            configuration.dataSource(
                    String.format("jdbc:postgresql://%s:%s/%s", network.host(), network.port(), storage.dbName()),
                    credentials.username(),
                    credentials.password()
            );
        };
    }
}
