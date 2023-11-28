package app.lumini.api.config.flyway;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(FlywayProperties.class)
class FlywayConfiguration {

    private final FlywayProperties flywayProperties;

    public FlywayConfiguration(FlywayProperties flywayProperties) {
        this.flywayProperties = flywayProperties;
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway() {
        return FlywayUtil.flyway(flywayProperties);
    }
}