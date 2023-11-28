package app.lumini.api;

import com.azure.spring.cloud.service.implementation.servicebus.factory.ServiceBusSenderClientBuilderFactory;
import com.azure.spring.messaging.AzureHeaders;
import com.azure.spring.messaging.checkpoint.Checkpointer;
import org.assertj.core.api.Assertions;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

import java.util.function.Supplier;

@AutoConfigureMockMvc
@SpringBootTest(classes = LuminiApiApplication.class)
@TestPropertySource(locations = "classpath:application-test.yaml")
@ActiveProfiles("test")
@ExtendWith({OutputCaptureExtension.class, MockitoExtension.class})
@Import(TestChannelBinderConfiguration.class)
class LuminiApiApplicationTests {

    @MockBean
    private ServiceBusSenderClientBuilderFactory serviceBusSenderClientBuilderFactory;

    @MockBean
    private Flyway flyway;

    @Autowired
    private ConfigurableApplicationContext context;

    @MockBean
    private Checkpointer checkpointer;

    @Autowired
    private Supplier<Flux<Message<String>>> supply1;

    @Autowired
    private Supplier<Flux<Message<String>>> supply2Skip;

    @Autowired
    private Sinks.Many<Message<String>> many;

    @Test
    public void testSupply1() {
        StepVerifier.create(supply1.get())
                .then(() -> many.tryEmitNext(MessageBuilder.withPayload("Test message").build()))
                .expectNextMatches(message -> "Test message".equals(message.getPayload()))
                .thenCancel()
                .verify();
    }

    @Test
    @DisplayName("exemplo de teste utilizando consumer com spring cloud stream")
    public void testSendAndReceiveMessage() {

        // Assemble
        InputDestination inputDestination = context.getBean(InputDestination.class);
        OutputDestination outputDestination = context.getBean(OutputDestination.class);

        Mockito.when(checkpointer.success()).thenReturn(Mono.empty());

        Message<byte[]> inputMessage = MessageBuilder
                .withPayload("Hello".getBytes())
                .setHeader(AzureHeaders.CHECKPOINTER, checkpointer)
                .build();

        // Actions
        inputDestination.send(inputMessage, "task");

        // Assertions
        Message<byte[]> outputMessage = outputDestination.receive(0, "task");
        Assertions.assertThat(outputMessage.getPayload()).isEqualTo("Hello".getBytes());
    }

}
