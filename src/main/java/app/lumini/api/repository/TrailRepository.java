package app.lumini.api.repository;

import app.lumini.api.model.Trail;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrailRepository extends R2dbcRepository<Trail, String> {

}
