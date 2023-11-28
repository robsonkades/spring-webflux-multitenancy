package app.lumini.api.config.r2dbc;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.r2dbc.connection.lookup.AbstractRoutingConnectionFactory;
import org.springframework.r2dbc.connection.lookup.ConnectionFactoryLookup;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;


public class MultiTenancyRoutingConnectionFactory extends AbstractRoutingConnectionFactory {

    private final MultiTenancyConnection multiTenancyConnection;
    private final Map<Object, ConnectionFactory> connectionFactoryMap = new ConcurrentHashMap<>();

    public MultiTenancyRoutingConnectionFactory(final MultiTenancyConnection multiTenancyConnection) {
        this.multiTenancyConnection = multiTenancyConnection;
    }

    @Override
    protected Mono<Object> determineCurrentLookupKey() {
        return Mono.deferContextual(Mono::just)
                .filter(contextView -> !contextView.isEmpty() && contextView.hasKey(TenantConstants.TENANT))
                .map(contextView -> contextView.get(TenantConstants.TENANT))
                .transform(objectMono -> fallbackIfEmpty(objectMono, Mono.just(TenantConstants.TENANT)));
    }

    @Override
    protected Mono<ConnectionFactory> determineTargetConnectionFactory() {
        return Mono.deferContextual(Mono::just)
                .filter(contextView -> contextView.hasKey(TenantConstants.TENANT))
                .map(contextView -> contextView.get(TenantConstants.TENANT))
                .flatMap(this::getConnectionFactoryMono)
                .switchIfEmpty(Mono.just(connectionFactoryMap.get(TenantConstants.TENANT)));
    }

    @Override
    public void setConnectionFactoryLookup(ConnectionFactoryLookup connectionFactoryLookup) {
        super.setConnectionFactoryLookup(connectionFactoryLookup);
    }

    @Override
    public void setTargetConnectionFactories(Map<?, ?> targetConnectionFactories) {
        connectionFactoryMap.putAll((Map<Object, ConnectionFactory>) targetConnectionFactories);
        super.setTargetConnectionFactories(connectionFactoryMap);
        super.afterPropertiesSet();
    }

    private Mono<ConnectionFactory> getConnectionFactoryMono(Object tenantId) {
        if (connectionFactoryMap.containsKey(tenantId)) {
            return super.determineTargetConnectionFactory();
        }
        return errorIfEmpty(multiTenancyConnection.resolve(tenantId), () -> new RuntimeException("Failed to obtain R2DBC Connection"))
                .flatMap(connectionFactory -> {
                    connectionFactoryMap.put(tenantId, connectionFactory);
                    setTargetConnectionFactories(connectionFactoryMap);
                    return super.determineTargetConnectionFactory();
                });
    }

    public static <R> Mono<R> errorIfEmpty(Mono<R> mono, Supplier<Throwable> throwableSupplier) {
        return mono.switchIfEmpty(Mono.defer(() -> Mono.error(throwableSupplier.get())));
    }

    public static <R> Mono<R> fallbackIfEmpty(Mono<R> mono, Mono<R> fallback) {
        return mono.switchIfEmpty(Mono.defer(() -> fallback));
    }
}
