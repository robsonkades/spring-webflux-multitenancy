package app.lumini.api.config.r2dbc;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.lookup.AbstractRoutingConnectionFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableR2dbcRepositories
public class MultiTenancyConfiguration extends AbstractR2dbcConfiguration {

    private final R2dbcProperties properties;

    public MultiTenancyConfiguration(final R2dbcProperties properties) {
        this.properties = properties;
    }

    @Bean
    public MultiTenancyConnection loadConnectionFactory() {
        return new MultiTenancyConnectionImpl();
    }

    @Override
    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionPool connectionPool = ConnectionFactoryUtil.getConnectionPool(properties);
        AbstractRoutingConnectionFactory routingConnectionFactory = new MultiTenancyRoutingConnectionFactory(loadConnectionFactory());
        routingConnectionFactory.setLenientFallback(false);
        routingConnectionFactory.setTargetConnectionFactories(Map.ofEntries(
                Map.entry(TenantConstants.TENANT, connectionPool)
        ));
        routingConnectionFactory.setDefaultTargetConnectionFactory(connectionPool);
        return routingConnectionFactory;
    }
}
