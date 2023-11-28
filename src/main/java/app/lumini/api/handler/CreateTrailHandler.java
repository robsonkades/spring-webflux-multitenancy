package app.lumini.api.handler;

import app.lumini.api.model.Trail;
import app.lumini.api.repository.TrailRepository;
import org.springframework.http.MediaType;
import org.springframework.validation.Errors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;


public class CreateTrailHandler extends AbstractValidationHandler<CreateTrailRequest, CreateTrailRequestValidator> {

    private final TrailRepository trailRepository;

    public CreateTrailHandler(final TrailRepository trailRepository) {
        super(CreateTrailRequest.class, new CreateTrailRequestValidator());
        this.trailRepository = trailRepository;
    }

    @Override
    protected Mono<ServerResponse> processBody(CreateTrailRequest body, ServerRequest originalRequest) {

        Trail trail = new Trail();
        trail.setId(UUID.randomUUID().toString());
        trail.setName(body.getName());
        trail.setDescription(body.getDescription());

        Mono<Trail> trailMono = trailRepository.save(trail);

        return ServerResponse.created(URI.create("/v1/trails/" + trail.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(trailMono, Trail.class);
    }

    @Override
    protected Mono<ServerResponse> onValidationErrors(Errors errors, CreateTrailRequest invalidBody, ServerRequest request) {
        return super.onValidationErrors(errors, invalidBody, request);
    }
}
