package app.lumini.api.amqp;

import com.azure.messaging.servicebus.ServiceBusReceiverAsyncClient;
import com.azure.messaging.servicebus.ServiceBusReceiverClient;
import com.azure.spring.cloud.service.implementation.servicebus.factory.ServiceBusReceiverClientBuilderFactory;
import com.azure.spring.cloud.stream.binder.servicebus.implementation.ServiceBusMessageChannelBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.binder.Binding;
import org.springframework.cloud.stream.binding.BindingsLifecycleController;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.Lifecycle;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.support.DefaultLifecycleProcessor;
import org.springframework.integration.endpoint.MessageSourcePollingTemplate;
import org.springframework.integration.endpoint.SourcePollingChannelAdapter;
import org.springframework.integration.monitor.IntegrationMBeanExporter;
import org.springframework.integration.support.management.ManageableLifecycle;
import org.springframework.integration.support.management.ManageableSmartLifecycle;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

//@Component
public class GracefulShutdownListener implements ApplicationListener<ContextClosedEvent> {

//    private final BindingsLifecycleController lifecycleController;
//
//    public GracefulShutdownListener(BindingsLifecycleController lifecycleController) {
//        this.lifecycleController = lifecycleController;
//    }

    //@Autowired
    //@Qualifier("companyUpdated_integrationflow.org.springframework.integration.config.ConsumerEndpointFactoryBean#0")
    //private SourcePollingChannelAdapter myPoller;
    //@Autowired
    //private Map<String, SourcePollingChannelAdapter> myPoller;
    //@Autowired
    private ApplicationContext applicationContext;


    // private final ServiceBusMessageChannelBinder serviceBusMessageChannelBinder;


    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        // Perform cleanup tasks here (e.g., closing database connections, flushing data to s3)
        // This method will be called when the Spring application context is closed (shutting down).
//        exporter.stopActiveChannels();
//        serviceBusReceiverAsyncClient.close();
       // MessageChannel streamBridge = applicationContext.getBean(MessageChannel.class);


//        lifecycleController.queryState("servicebus")
//                .stream()
//                .forEach(binding -> {
//
//                    System.out.println("Graceful termination");
//                });

        System.out.println("Graceful termination");
    }
}