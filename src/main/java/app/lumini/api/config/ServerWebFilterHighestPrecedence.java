package app.lumini.api.config;

import app.lumini.api.config.r2dbc.TenantConstants;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ServerWebFilterHighestPrecedence implements WebFilter {

    private static final List<String> IGNORED_PATHS = List.of("/webjars", "/v3/api-docs", "/path3");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        if (isIgnoredPath(path)) {
            return chain.filter(exchange);
        }

        return applyFilterLogic(exchange, chain);
    }

    private Mono<Void> applyFilterLogic(ServerWebExchange exchange, WebFilterChain chain) {

        List<String> headers = exchange.getRequest().getHeaders().get("X-Tenant");

        if(headers == null || headers.isEmpty()) {
            return chain.filter(exchange);
        }

        String tenantKey = headers.get(0);
        return chain
                .filter(exchange)
                .contextWrite(ctx -> ctx.put(TenantConstants.TENANT, tenantKey));
    }

    private boolean isIgnoredPath(String path) {
        return IGNORED_PATHS.stream().anyMatch(path::startsWith);
    }
}
