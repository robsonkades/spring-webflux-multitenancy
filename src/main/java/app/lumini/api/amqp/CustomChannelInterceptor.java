package app.lumini.api.amqp;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;

public class CustomChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // Código de lógica antes de a mensagem ser enviada
        System.out.println("Mensagem interceptada antes do envio: " + message);
        // Você pode modificar a mensagem se necessário
        return MessageBuilder.withPayload(message.getPayload().toString().toUpperCase()).copyHeadersIfAbsent(message.getHeaders()).build();
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        // Código de lógica após a mensagem ser enviada
        System.out.println("Mensagem interceptada após o envio: " + message);
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        // Código de lógica após a conclusão do envio
        System.out.println("Mensagem interceptada após a conclusão do envio: " + message);
    }

    // Implementar outros métodos conforme necessário
}
