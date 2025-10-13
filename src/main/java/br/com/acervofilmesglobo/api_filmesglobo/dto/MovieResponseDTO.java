package br.com.acervofilmesglobo.api_filmesglobo.dto;

import br.com.acervofilmesglobo.api_filmesglobo.model.Movie;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MovieResponseDTO {

    private Long idMovie;
    private String portugueseTitle;
    private String posterUrl;
    private int screeningsTotal;
    private String screeningsUrl;

    public MovieResponseDTO(Movie movie){
        this.idMovie = movie.getIdMovie();
        this.portugueseTitle= movie.getPortugueseTitle();
        this.posterUrl = movie.getPosterUrl();
        this.screeningsTotal = movie.getScreeningsTotal();
        this.screeningsUrl = "/api/movies/" + movie.getIdMovie() + "/screenings";
    }

}
