package app.lumini.api.config.r2dbc;

import io.r2dbc.pool.ConnectionPool;
import reactor.core.publisher.Mono;

public interface MultiTenancyConnection {

    Mono<ConnectionPool> resolve(Object value);
}
