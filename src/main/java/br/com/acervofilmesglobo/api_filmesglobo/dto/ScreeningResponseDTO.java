package br.com.acervofilmesglobo.api_filmesglobo.dto;

import br.com.acervofilmesglobo.api_filmesglobo.model.Screening;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ScreeningResponseDTO {

    private String portugueseTitle;
    private LocalDate screeningDate;
    private String session;

    public ScreeningResponseDTO(Screening screening){
        this.portugueseTitle = screening.getMovie().getPortugueseTitle();
        this.screeningDate = screening.getScreeningDate();
        this.session = screening.getSession();
    }

}
