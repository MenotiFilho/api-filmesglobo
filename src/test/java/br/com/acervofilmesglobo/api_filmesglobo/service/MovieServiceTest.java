package br.com.acervofilmesglobo.api_filmesglobo.service;

import br.com.acervofilmesglobo.api_filmesglobo.dto.ScreeningLoadDTO;
import br.com.acervofilmesglobo.api_filmesglobo.model.Movie;
import br.com.acervofilmesglobo.api_filmesglobo.repository.MovieRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para a Camada de Serviço de Filmes")
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    @Test
    @DisplayName("Deve criar um novo filme e sua primeira exibição quando o filme não existe")
    void processScreeningLoad_whenMovieDoesNotExist_shouldCreateNewMovieAndScreening() {
        ScreeningLoadDTO dto = new ScreeningLoadDTO();
        dto.setOriginalTitle("Inception");
        dto.setPortugueseTitle("A Origem");
        dto.setReleaseYear(2010);
        dto.setScreeningDate(LocalDate.of(2025, 10, 11));

        when(movieRepository.findByOriginalTitle("Inception")).thenReturn(Optional.empty());

        movieService.processScreeningLoad(List.of(dto));

        ArgumentCaptor<Movie> movieCaptor = ArgumentCaptor.forClass(Movie.class);

        verify(movieRepository, times(1)).save(movieCaptor.capture());

        Movie savedMovie = movieCaptor.getValue();

        assertThat(savedMovie.getOriginalTitle()).isEqualTo("Inception");
        assertThat(savedMovie.getReleaseYear()).isEqualTo(2010);
        assertThat(savedMovie.getScreeningHistory()).hasSize(1);
        assertThat(savedMovie.getScreeningHistory().get(0).getScreeningDate()).isEqualTo(LocalDate.of(2025, 10, 11));

    }

    @Test
    @DisplayName("Filme já existe e so deve adicionar uma nova exibição")
    void processScreeningLoad_whenMovieExists_shouldCreateAddANewScreening() {

    }
}
