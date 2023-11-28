package app.lumini.api.handler;

import app.lumini.api.model.Trail;
import app.lumini.api.repository.TrailRepository;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class UpdateTrailHandler {

    private final TrailRepository trailRepository;

    public UpdateTrailHandler(final TrailRepository trailRepository) {
        this.trailRepository = trailRepository;
    }

    public Mono<ServerResponse> execute(final ServerRequest request) {
        Flux<Trail> toUpdate = trailRepository
                .findAll()
                .map(trail -> {
                    trail.setDescription("Description 1");
                    return trail;
                }).flatMap(trailRepository::save);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(toUpdate, Trail.class);
    }
}
