package app.lumini.api.router;

import app.lumini.api.handler.CreateTrailHandler;
import app.lumini.api.handler.CreateTrailRequest;
import app.lumini.api.handler.UpdateTrailHandler;
import app.lumini.api.model.Trail;
import app.lumini.api.repository.TrailRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;


@Configuration
public class TrailRouter {

    private final TrailRepository trailRepository;

    public TrailRouter(final TrailRepository trailRepository) {
        this.trailRepository = trailRepository;
    }

    @RouterOperations({
            @RouterOperation(
                    method = RequestMethod.POST,
                    path = "/api/trails",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    consumes = {MediaType.APPLICATION_JSON_VALUE},
                    operation = @Operation(
                            description = "Cria uma trilha",
                            operationId = "createTrail",
                            tags = "Trilhas",
                            requestBody = @RequestBody(description = "Cria uma trilha", required = true, content = @Content(schema = @Schema(implementation = CreateTrailRequest.class))),
                            responses = {
                                    @ApiResponse(responseCode = "201",
                                            headers = {
                                                    @Header(name = "content-type", required = true),
                                                    @Header(name = "location", required = true),
                                            },
                                            description = "Created",
                                            content = @Content(schema = @Schema(implementation = Trail.class))),
                                    @ApiResponse(responseCode = "400",
                                            description = "Bad Request",
                                            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
                            }
                    )
            )
    })
    @Bean
    public RouterFunction<ServerResponse> router() {
        CreateTrailHandler handler = new CreateTrailHandler(trailRepository);
        UpdateTrailHandler updateTrailHandler = new UpdateTrailHandler(trailRepository);
        return RouterFunctions
                .route(GET("/api/trails").and(contentType(APPLICATION_JSON)), updateTrailHandler::execute)
                .andRoute(POST("/api/trails").and(accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON))), handler::execute)
                .andRoute(POST("/api/trails/rollback").and(accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON))), this::saveRollback);

    }

    @Transactional
    public Mono<ServerResponse> saveRollback(ServerRequest serverRequest) {
        Trail trail = new Trail();
        trail.setId(UUID.randomUUID().toString());
        trail.setName("Transactional");

        Mono<Trail> trailMono = trailRepository.save(trail);

        if (true) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "ERROR ROLLBACK");
        }

        return ServerResponse.created(URI.create("/v1/trails/" + trail.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(trailMono, Trail.class);
    }
}
