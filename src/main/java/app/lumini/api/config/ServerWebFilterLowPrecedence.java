package app.lumini.api.config;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

//@Component
public class ServerWebFilterLowPrecedence implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        System.out.println("FILTER BEFORE AQUI");
        return chain.filter(exchange).doFinally(signalType -> {
            System.out.println("FILTER AFTER AQUI");
        });
    }
}
