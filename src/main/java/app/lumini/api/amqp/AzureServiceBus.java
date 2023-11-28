package app.lumini.api.amqp;

import com.azure.spring.messaging.checkpoint.Checkpointer;
import datadog.trace.api.CorrelationIdentifier;
import datadog.trace.api.Trace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.azure.spring.messaging.AzureHeaders.CHECKPOINTER;

@Configuration
public class AzureServiceBus {

    private static final Logger LOGGER = LoggerFactory.getLogger(AzureServiceBus.class);

    @Bean
    public Sinks.Many<Message<Notify>> many() {
        return Sinks.many().multicast().directBestEffort();
    }

    @Bean
    public Supplier<Flux<Message<Notify>>> supply1(Sinks.Many<Message<Notify>> many) {
        return () -> many.asFlux()
                .log()
                .doOnNext(m -> LOGGER.info("supply: Manually sending message {}", m))
                .doOnError(t -> LOGGER.error("Error encountered", t));
    }

    @Bean
    public Supplier<Flux<Message<Notify>>> supply2(Sinks.Many<Message<Notify>> many) {
        return () -> many.asFlux()
                .doOnNext(m -> LOGGER.info("supply: Manually sending message {}", m))
                .doOnError(t -> LOGGER.error("Error encountered", t));
    }

    @Bean
    public Consumer<Message<Notify>> task() {
        return message -> {
             MDC.put("correlationId", CorrelationIdentifier.getTraceId());;
            Checkpointer checkpointer = (Checkpointer) message.getHeaders().get(CHECKPOINTER);

            if (!true) {
                throw new IllegalArgumentException("D");
            }
            checkpointer.success()
                    .doOnSuccess(s -> LOGGER.info("Task '{}' successfully checkpointed", message.getPayload()))
                    .doOnError(e -> LOGGER.error("Error found", e))
                    .subscribe();
        };
    }

    @Bean
    public Consumer<Message<Notify>> dlq() {
        return message -> {
            MDC.put("correlationId", CorrelationIdentifier.getTraceId());;
            Checkpointer checkpointer = (Checkpointer) message.getHeaders().get(CHECKPOINTER);

            checkpointer.success()
                    .doOnSuccess(s -> LOGGER.info("Task '{}' successfully checkpointed", message.getPayload()))
                    .doOnError(e -> LOGGER.error("Error found", e))
                    .subscribe();
        };
    }
}
