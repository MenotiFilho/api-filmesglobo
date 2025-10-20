package br.com.acervofilmesglobo.api_filmesglobo.service;

import br.com.acervofilmesglobo.api_filmesglobo.dto.MovieDetailDTO;
import br.com.acervofilmesglobo.api_filmesglobo.dto.MovieResponseDTO;
import br.com.acervofilmesglobo.api_filmesglobo.dto.ScreeningLoadDTO;
import br.com.acervofilmesglobo.api_filmesglobo.dto.ScreeningResponseDTO;
import br.com.acervofilmesglobo.api_filmesglobo.model.Movie;
import br.com.acervofilmesglobo.api_filmesglobo.model.Screening;
import br.com.acervofilmesglobo.api_filmesglobo.repository.MovieRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Transactional
    public void processScreeningLoad(List<ScreeningLoadDTO> screeningLoadDTOS) {
        for (ScreeningLoadDTO dto : screeningLoadDTOS) {
            Movie movie = movieRepository.findByOriginalTitle(dto.getOriginalTitle()).orElse(new Movie());

            movie.setOriginalTitle(dto.getOriginalTitle());
            movie.setPortugueseTitle(dto.getPortugueseTitle());
            movie.setReleaseYear(dto.getReleaseYear());
            movie.setSummary(dto.getSummary());
            movie.setPosterUrl(dto.getPosterUrl());

            boolean screeningExists = movie.getScreeningHistory().stream()
                    .anyMatch(screening ->
                            screening.getScreeningDate().equals(dto.getScreeningDate()) &&
                                    screening.getSession().equalsIgnoreCase(dto.getSession())
                    );

            if (!screeningExists) {
                Screening newScreening = new Screening();
                newScreening.setScreeningDate(dto.getScreeningDate());
                newScreening.setSession(dto.getSession());
                newScreening.setMovie(movie);
                movie.getScreeningHistory().add(newScreening);
            }

            movieRepository.save(movie);
        }
    }

    @Transactional(readOnly = true)
    public Page<MovieResponseDTO> findAll(Pageable pageable) {
        Page<Movie> moviePage = movieRepository.findAll(pageable);

        return moviePage.map(MovieResponseDTO::new);
    }



    @Transactional(readOnly = true)
    public List<ScreeningResponseDTO> findScreeningsByMovieId(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Filme não encontrado com o ID: " + movieId));

        return movie.getScreeningHistory().stream()
                .map(ScreeningResponseDTO::new)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public MovieDetailDTO findById(Long movieId){
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(()->new EntityNotFoundException("Filme não encontrado com o ID: " + movieId));

        return new MovieDetailDTO(movie);
    }

    @Transactional(readOnly = true)
    public Page<MovieResponseDTO> searchByPortugueseTitle(String portugueseTitle, Pageable pageable) {
        Page<Movie> moviePage  = movieRepository.findByPortugueseTitleContainingIgnoreCase(portugueseTitle, pageable);

        return moviePage.map(MovieResponseDTO::new);
    }
}
