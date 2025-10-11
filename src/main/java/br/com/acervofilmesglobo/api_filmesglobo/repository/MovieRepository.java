package br.com.acervofilmesglobo.api_filmesglobo.repository;

import br.com.acervofilmesglobo.api_filmesglobo.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByOriginalTitle(String originalTitle);
}
