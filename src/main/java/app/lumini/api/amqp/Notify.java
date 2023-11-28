package app.lumini.api.amqp;

import java.util.UUID;

public class Notify {

    private UUID id;
    private String message;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Notify{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }
}
