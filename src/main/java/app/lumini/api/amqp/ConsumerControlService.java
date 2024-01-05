package app.lumini.api.amqp;

import com.azure.messaging.servicebus.ServiceBusReceiverAsyncClient;
import com.azure.spring.cloud.stream.binder.servicebus.core.properties.ServiceBusConsumerProperties;
import com.azure.spring.cloud.stream.binder.servicebus.core.properties.ServiceBusProducerProperties;
import com.azure.spring.cloud.stream.binder.servicebus.implementation.ServiceBusMessageChannelBinder;
import com.azure.spring.messaging.listener.MessageListenerContainer;
import com.azure.spring.messaging.servicebus.core.listener.ServiceBusMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.ExtendedPropertiesBinder;
import org.springframework.cloud.stream.binding.BindingsLifecycleController;
import org.springframework.integration.endpoint.MessageSourcePollingTemplate;
import org.springframework.integration.endpoint.PollingConsumer;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service
public class ConsumerControlService {

//    private final ServiceBusMessageChannelBinder lifecycleController;
//
//    public ConsumerControlService(ServiceBusMessageChannelBinder lifecycleController) {
//        this.lifecycleController = lifecycleController;
//    }



    public void pauseConsumer() {

    }

    public void resumeConsumer() {
    }
}
