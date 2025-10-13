package br.com.acervofilmesglobo.api_filmesglobo.service;

import br.com.acervofilmesglobo.api_filmesglobo.dto.ScreeningLoadDTO;
import br.com.acervofilmesglobo.api_filmesglobo.model.Movie;
import br.com.acervofilmesglobo.api_filmesglobo.model.Screening;
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
        dto.setSession("Tela Quente");

        when(movieRepository.findByOriginalTitle("Inception")).thenReturn(Optional.empty());

        movieService.processScreeningLoad(List.of(dto));

        ArgumentCaptor<Movie> movieCaptor = ArgumentCaptor.forClass(Movie.class);
        verify(movieRepository, times(1)).save(movieCaptor.capture());
        Movie savedMovie = movieCaptor.getValue();

        assertThat(savedMovie.getOriginalTitle()).isEqualTo("Inception");
        assertThat(savedMovie.getScreeningHistory()).hasSize(1);
        assertThat(savedMovie.getScreeningHistory().get(0).getScreeningDate()).isEqualTo(LocalDate.of(2025, 10, 11));
        assertThat(savedMovie.getScreeningHistory().get(0).getSession()).isEqualTo("Tela Quente");
    }

    @Test
    @DisplayName("Deve adicionar uma nova exibição a um filme já existente")
    void processScreeningLoad_whenMovieExists_shouldAddANewScreening() {

        Movie existingMovie = new Movie();
        existingMovie.setOriginalTitle("Back to the Future");
        existingMovie.setReleaseYear(1985);
        Screening oldScreening = new Screening();
        oldScreening.setScreeningDate(LocalDate.of(2014, 10, 26));
        oldScreening.setSession("Sessão da Tarde");
        oldScreening.setMovie(existingMovie);
        existingMovie.getScreeningHistory().add(oldScreening);

        ScreeningLoadDTO newDto = new ScreeningLoadDTO();
        newDto.setOriginalTitle("Back to the Future");
        newDto.setScreeningDate(LocalDate.of(2025, 10, 11));
        newDto.setSession("Tela Quente");

        when(movieRepository.findByOriginalTitle("Back to the Future")).thenReturn(Optional.of(existingMovie));

        movieService.processScreeningLoad(List.of(newDto));

        ArgumentCaptor<Movie> movieCaptor = ArgumentCaptor.forClass(Movie.class);
        verify(movieRepository).save(movieCaptor.capture());
        Movie savedMovie = movieCaptor.getValue();

        assertThat(savedMovie.getScreeningHistory()).hasSize(2);
        assertThat(savedMovie.getScreeningHistory())
                .extracting(Screening::getSession)
                .contains("Sessão da Tarde", "Tela Quente");
    }

    @Test
    @DisplayName("Não deve adicionar exibição se data e sessão já existem")
    void processScreeningLoad_whenMovieAndScreeningAlreadyExist_shouldDoNothing() {

        Movie existingMovie = new Movie();
        existingMovie.setOriginalTitle("Back to the Future");
        Screening existingScreening = new Screening();
        existingScreening.setScreeningDate(LocalDate.of(2025, 10, 11));
        existingScreening.setSession("Sessão da Tarde");
        existingScreening.setMovie(existingMovie);
        existingMovie.getScreeningHistory().add(existingScreening);

        ScreeningLoadDTO duplicateDto = new ScreeningLoadDTO();
        duplicateDto.setOriginalTitle("Back to the Future");
        duplicateDto.setScreeningDate(LocalDate.of(2025, 10, 11));
        duplicateDto.setSession("Sessão da Tarde");

        when(movieRepository.findByOriginalTitle("Back to the Future")).thenReturn(Optional.of(existingMovie));

        movieService.processScreeningLoad(List.of(duplicateDto));

        ArgumentCaptor<Movie> movieCaptor = ArgumentCaptor.forClass(Movie.class);
        verify(movieRepository).save(movieCaptor.capture());
        Movie savedMovie = movieCaptor.getValue();

        assertThat(savedMovie.getScreeningHistory()).hasSize(1);
    }

    @Test
    @DisplayName("Deve adicionar exibição se a data é a mesma mas a sessão é diferente")
    void processScreeningLoad_whenDateIsSameButSessionIsDifferent_shouldAddScreening() {

        Movie existingMovie = new Movie();
        existingMovie.setOriginalTitle("The Goonies");
        Screening existingScreening = new Screening();
        existingScreening.setScreeningDate(LocalDate.of(2025, 11, 22));
        existingScreening.setSession("Sessão da Tarde");
        existingScreening.setMovie(existingMovie);
        existingMovie.getScreeningHistory().add(existingScreening);

        ScreeningLoadDTO newDto = new ScreeningLoadDTO();
        newDto.setOriginalTitle("The Goonies");
        newDto.setScreeningDate(LocalDate.of(2025, 11, 22));
        newDto.setSession("Corujão");

        when(movieRepository.findByOriginalTitle("The Goonies")).thenReturn(Optional.of(existingMovie));

        movieService.processScreeningLoad(List.of(newDto));

        ArgumentCaptor<Movie> movieCaptor = ArgumentCaptor.forClass(Movie.class);
        verify(movieRepository).save(movieCaptor.capture());
        Movie savedMovie = movieCaptor.getValue();

        assertThat(savedMovie.getScreeningHistory()).hasSize(2);
        assertThat(savedMovie.getScreeningHistory())
                .extracting(Screening::getSession)
                .contains("Sessão da Tarde", "Corujão");
    }
}