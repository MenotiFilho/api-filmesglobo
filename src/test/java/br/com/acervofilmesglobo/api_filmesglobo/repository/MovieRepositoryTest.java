package br.com.acervofilmesglobo.api_filmesglobo.repository;

import br.com.acervofilmesglobo.api_filmesglobo.model.Movie;
import br.com.acervofilmesglobo.api_filmesglobo.model.Screening;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Testes para o Movie Repository")
public class MovieRepositoryTest {
    @Autowired
    private MovieRepository movieRepository;

    @Test
    @DisplayName("Deve salvar um filme com seu histórico de exibição com sucesso")
    void saveMovie_WithScreeningHistory_ShouldPersistData(){
        Movie movie = new Movie();
        movie.setOriginalTitle("Back to the Future");
        movie.setPortugueseTitle("De Volta para o Futuro");
        movie.setReleaseYear(1985);
        movie.setSummary("Marty McFly viaja para 1955 com a máquina do tempo do cientista Dr. Brown. Ele deve garantir que seus pais se apaixonem, para não arriscar sua própria existência.");
        movie.setPosterUrl("https://cdn11.bigcommerce.com/s-yzgoj/images/stencil/1280x1280/products/2930371/5899116/MOVIB82743__55442.1679556602.jpg?c=2");

        Screening screening = new Screening();
        screening.setScreeningDate(LocalDate.of(2025,10,26));
        screening.setMovie(movie);
        screening.setSession("Sessão da Tarde");

        movie.getScreeningHistory().add(screening);

        Movie savedMovie = movieRepository.save(movie);

        assertThat(savedMovie).isNotNull();
        assertThat(savedMovie.getIdMovie()).isNotNull();
        assertThat(savedMovie.getOriginalTitle()).isEqualTo("Back to the Future");
        assertThat(savedMovie.getScreeningHistory()).isNotEmpty();
        assertThat(savedMovie.getScreeningHistory().get(0).getScreeningDate()).isEqualTo(LocalDate.of(2025,10,26));
    }

    @Test
    @DisplayName("Deve encontrar um filme pelo seu título original")
    void findByOriginalTitle_WhenMovieExists_ShouldReturnMovie() {
        Movie movie = new Movie();
        movie.setOriginalTitle("The Matrix");
        movie.setPortugueseTitle("Matrix");
        movie.setReleaseYear(1999);
        movieRepository.save(movie);

        Optional<Movie> foundMovieOptional = movieRepository.findByOriginalTitle("The Matrix");

        assertThat(foundMovieOptional).isPresent();
        assertThat(foundMovieOptional.get().getReleaseYear()).isEqualTo(1999);
    }
}
