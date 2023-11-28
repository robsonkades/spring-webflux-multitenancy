package app.lumini.api.handler;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "MeuNovoNomeDeSchema")
public class CreateTrailRequest {

    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
