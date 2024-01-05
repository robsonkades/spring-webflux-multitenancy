package app.lumini.api.config;

import com.azure.spring.cloud.core.customizer.AzureServiceClientBuilderCustomizer;
import com.azure.spring.cloud.stream.binder.servicebus.config.ServiceBusProducerFactoryCustomizer;
import com.azure.spring.messaging.implementation.config.AzureMessageListenerContainerFactoryAdapter;
import com.azure.spring.messaging.listener.AbstractMessageListenerContainer;
import com.azure.spring.messaging.servicebus.core.DefaultServiceBusNamespaceProcessorFactory;
import com.azure.spring.messaging.servicebus.core.DefaultServiceBusNamespaceProducerFactory;
import com.azure.spring.messaging.servicebus.core.ServiceBusProducerFactory;
import com.azure.spring.messaging.servicebus.core.listener.ServiceBusMessageListenerContainer;
import com.azure.spring.messaging.servicebus.implementation.core.config.ServiceBusMessageListenerContainerFactory;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cloud.stream.config.ListenerContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.time.format.DateTimeFormatter;

@Configuration
public class ServerConfig implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/*")
                .allowedOrigins("*");
    }


//    @Bean
//    public ServiceBusProducerFactoryCustomizer<ServiceBusProducerFactory> customizer() {
//        return (container) -> {
//
//
//
//        };
//    }

//    @Bean
//    public TaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//        taskExecutor.setCorePoolSize(2);
//        taskExecutor.setMaxPoolSize(2);
//        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
//        taskExecutor.initialize();
//        return taskExecutor;
//    }
}
