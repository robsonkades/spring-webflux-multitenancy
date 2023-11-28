package app.lumini.api.amqp;

import com.azure.spring.messaging.AzureHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
public class AzureServiceBusController {

    @Autowired
    private Sinks.Many<Message<Notify>> many;

    @PostMapping("/messages")
    public ResponseEntity<String> sendMessage(@RequestParam String message) {

        Notify notify = new Notify();
        notify.setId(UUID.randomUUID());
        notify.setMessage(message);


        Message<Notify> build = MessageBuilder
                .withPayload(notify)
                .setHeader(AzureHeaders.SCHEDULED_ENQUEUE_MESSAGE, 30000)
                .build();

        many.emitNext(build, Sinks.EmitFailureHandler.FAIL_FAST);
        return ResponseEntity.ok("Sent!");
    }
}
