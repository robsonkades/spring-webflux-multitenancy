package app.lumini.api.amqp;

import com.azure.spring.messaging.AzureHeaders;
import io.micrometer.observation.ObservationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.function.cloudevent.CloudEventMessageBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.integration.channel.interceptor.ObservationPropagationChannelInterceptor;
import org.springframework.integration.config.GlobalChannelInterceptor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Configuration
public class IntegrationConfig {


    @Bean
    @GlobalChannelInterceptor(order = Ordered.HIGHEST_PRECEDENCE)
    public ObservationPropagationChannelInterceptor observationPropagationChannelInterceptor(ObservationRegistry observationRegistry) {
        return new ObservationPropagationChannelInterceptor(observationRegistry);
    }

//    private final static Logger log = LoggerFactory.getLogger(IntegrationConfig.class);
//
//    @Bean
//    @GlobalChannelInterceptor(patterns = {"*-in-0"}) // O padrão corresponde ao nome dos canais de entrada
//    public ChannelInterceptor inputChannelInterceptor() {
//        return new ChannelInterceptor() {
//            @Override
//            public Message<?> preSend(Message<?> message, MessageChannel channel) {
//                log.info("b");
//                // Lógica de interceptação antes da mensagem ser enviada para o consumidor
//                //return MessageBuilder.withPayload(message.getPayload().toString().toUpperCase()).copyHeadersIfAbsent(message.getHeaders()).build();
//                return message;
//            }
//        };
//    }
//
//    @Bean
//    @GlobalChannelInterceptor(patterns = {"*-out-0"}) // O padrão corresponde ao nome dos canais de entrada
//    public ChannelInterceptor outputChannelInterceptor() {
//        return new ChannelInterceptor() {
//            @Override
//            public Message<?> preSend(Message<?> message, MessageChannel channel) {
//                // Lógica de interceptação antes da mensagem ser enviada para o consumidor
//                //return MessageBuilder.withPayload(message.getPayload().toString().toUpperCase()).copyHeadersIfAbsent(message.getHeaders()).build();
//                return CloudEventMessageBuilder.withData(message.getPayload())
//                        .setHeader("X-TenantId", UUID.randomUUID().toString())
//                        .copyHeaders(message.getHeaders())
//                        .build();
////                    return MessageBuilder
////                            .withPayload(cloudEvent)
////                            .copyHeadersIfAbsent(message.getHeaders())
////                            .setHeader("content-type", "application/cloudevents+json")
////                            .build();
//            }
//        };
//    }
}
