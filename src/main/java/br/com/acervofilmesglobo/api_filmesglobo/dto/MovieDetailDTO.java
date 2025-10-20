package br.com.acervofilmesglobo.api_filmesglobo.dto;

import br.com.acervofilmesglobo.api_filmesglobo.model.Movie;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MovieDetailDTO {
    private Long idMovie;
    private String originalTitle;
    private String portugueseTitle;
    private Integer releaseYear;
    private String summary;
    private String posterUrl;
    private int screeningsTotal;
    private List<ScreeningResponseDTO> screeningHistory;

    public MovieDetailDTO(Movie movie) {
        this.idMovie = movie.getIdMovie();
        this.originalTitle = movie.getOriginalTitle();
        this.portugueseTitle = movie.getPortugueseTitle();
        this.releaseYear = movie.getReleaseYear();
        this.summary = movie.getSummary();
        this.posterUrl = movie.getPosterUrl();
        this.screeningsTotal = movie.getScreeningsTotal();
        this.screeningHistory = movie.getScreeningHistory().stream()
                .map(ScreeningResponseDTO::new)
                .toList();
    }
}
