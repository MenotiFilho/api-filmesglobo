package br.com.acervofilmesglobo.api_filmesglobo.service;

import br.com.acervofilmesglobo.api_filmesglobo.dto.ScreeningLoadDTO;
import br.com.acervofilmesglobo.api_filmesglobo.model.Movie;
import br.com.acervofilmesglobo.api_filmesglobo.model.Screening;
import br.com.acervofilmesglobo.api_filmesglobo.repository.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;
    
    @Transactional
    public void processScreeningLoad(List<ScreeningLoadDTO> screeningLoadDTOS){
        for (ScreeningLoadDTO dto : screeningLoadDTOS){
            Movie movie = movieRepository.findByOriginalTitle(dto.getOriginalTitle()).orElse(new Movie());

            movie.setOriginalTitle(dto.getOriginalTitle());
            movie.setPortugueseTitle(dto.getPortugueseTitle());
            movie.setReleaseYear(dto.getReleaseYear());
            movie.setSummary(dto.getSummary());
            movie.setPosterUrl(dto.getPosterUrl());

            boolean screeningExists = movie.getScreeningHistory().stream().anyMatch(screening -> screening.getScreeningDate().equals(dto.getScreeningDate()));

            if (!screeningExists){
                Screening newScreening = new Screening();
                newScreening.setScreeningDate(dto.getScreeningDate());
                newScreening.setMovie(movie);
                movie.getScreeningHistory().add(newScreening);
            }

            movieRepository.save(movie);
        }
    }
}
