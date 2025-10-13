package br.com.acervofilmesglobo.api_filmesglobo.controller;

import br.com.acervofilmesglobo.api_filmesglobo.dto.MovieResponseDTO;
import br.com.acervofilmesglobo.api_filmesglobo.dto.ScreeningLoadDTO;
import br.com.acervofilmesglobo.api_filmesglobo.dto.ScreeningResponseDTO;
import br.com.acervofilmesglobo.api_filmesglobo.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;
    private final String apiKey;

    public MovieController(MovieService movieService, @Value("${api.security.key}") String apiKey) {
        this.movieService = movieService;
        this.apiKey = apiKey;
    }

    @PostMapping("/load")
    public ResponseEntity<Void> loadScreenings(@RequestHeader("X-API-KEY") String requestApiKey, @RequestBody @Valid List<ScreeningLoadDTO> screeningLoadDTOS) {
        if (!apiKey.equals(requestApiKey)) {
            return ResponseEntity.status(401).build();
        }

        movieService.processScreeningLoad(screeningLoadDTOS);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<MovieResponseDTO>> listAll(Pageable pageable) {
        Page<MovieResponseDTO> moviePage = movieService.findAll(pageable);

        return ResponseEntity.ok(moviePage);
    }

    @GetMapping("/{id}/screenings")
    public ResponseEntity<List<ScreeningResponseDTO>> listScreeningsByMovie(@PathVariable Long id){
        List<ScreeningResponseDTO> screenings = movieService.findScreeningsByMovieId(id);
        return ResponseEntity.ok(screenings);
    }
}
