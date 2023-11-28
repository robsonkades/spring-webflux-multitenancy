package app.lumini.api.config.r2dbc;

import app.lumini.api.config.flyway.FlywayUtil;
import io.r2dbc.pool.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class MultiTenancyConnectionImpl implements MultiTenancyConnection {

    private final static Logger LOGGER = LoggerFactory.getLogger(MultiTenancyConnectionImpl.class);

    @Override
    public Mono<ConnectionPool> resolve(Object value) {

        if (value.equals("tenant1")) {
            R2dbcProperties properties = new R2dbcProperties();
            properties.setUrl("r2dbc:postgresql://localhost:5432/postgres");
            properties.setUsername("postgres");
            properties.setPassword("postgres");
            properties.getProperties().put("schema", "tenant1");

            FlywayProperties flywayProperties = new FlywayProperties();
            flywayProperties.setDefaultSchema("tenant1");
            flywayProperties.setUrl("jdbc:postgresql://localhost:5432/postgres");
            flywayProperties.setUser("postgres");
            flywayProperties.setPassword("postgres");

            return Mono.deferContextual(Mono::just)
                    .map(contextView -> contextView.get(TenantConstants.TENANT))
                    .doOnNext(o -> LOGGER.info("Tenant context with name {}", o))
                    .map(o -> FlywayUtil.flyway(flywayProperties).migrate())
                    .map(migrateResult -> ConnectionFactoryUtil.getConnectionPool(properties))
                    .subscribeOn(Schedulers.boundedElastic());

        } else if (value.equals("tenant2")) {
            R2dbcProperties properties = new R2dbcProperties();
            properties.setUrl("r2dbc:postgresql://localhost:5432/postgres");
            properties.setUsername("postgres");
            properties.setPassword("postgres");
            properties.getProperties().put("schema", "tenant2");

            FlywayProperties flywayProperties = new FlywayProperties();
            flywayProperties.setDefaultSchema("tenant2");
            flywayProperties.setUrl("jdbc:postgresql://localhost:5432/postgres");
            flywayProperties.setUser("postgres");
            flywayProperties.setPassword("postgres");

            return Mono.deferContextual(Mono::just)
                    .map(contextView -> contextView.get(TenantConstants.TENANT))
                    .doOnNext(tenantId -> LOGGER.info("Tenant context with name {}", tenantId))
                    .map(o -> FlywayUtil.flyway(flywayProperties).migrate())
                    .map(migrateResult -> ConnectionFactoryUtil.getConnectionPool(properties))
                    .subscribeOn(Schedulers.boundedElastic());
        }

        return Mono.error(new RuntimeException(" Not found"));
    }
}
