package app.lumini.api.amqp;

import com.azure.spring.messaging.AzureHeaders;
import com.azure.spring.messaging.checkpoint.Checkpointer;
import datadog.trace.api.Trace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.interceptor.ObservationPropagationChannelInterceptor;
import org.springframework.messaging.Message;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Configuration
public class CompanyUpdatedConsumer implements Consumer<Message<Company>>, SmartLifecycle {

    private boolean isRunning = false;
    private static final Logger LOGGER = LoggerFactory.getLogger(AzureServiceBus.class);

    @Override
    @Trace
    public void accept(Message<Company> companyMessage) {
        if (!isRunning) {
            return;
        }

        try {
            Checkpointer checkpointer = companyMessage.getHeaders().get(AzureHeaders.CHECKPOINTER, Checkpointer.class);
            String tenant = companyMessage.getHeaders().get("tenant", String.class);
            TenantThreadLocalContext.set(tenant);

            TimeUnit.SECONDS.sleep(5);
            checkpointer.success()
                    .doOnSuccess(s -> LOGGER.info("companyUpdatedConsumer '{}' successfully checkpointed", companyMessage.getPayload()))
                    .doOnError(e -> LOGGER.error("Error found", e))
                    .subscribe();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            TenantThreadLocalContext.unset();
        }

    }

    @Override
    public void start() {
        isRunning = true;
    }

    @Override
    public void stop() {
        isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }
}
