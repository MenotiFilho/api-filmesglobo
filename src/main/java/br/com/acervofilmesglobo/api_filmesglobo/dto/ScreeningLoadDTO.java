package br.com.acervofilmesglobo.api_filmesglobo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ScreeningLoadDTO {

    @NotBlank(message = "Título original não pode ser vazio")
    private String originalTitle;

    @NotBlank(message = "Título em português não pode ser vazio")
    private String portugueseTitle;

    @NotNull(message = "Ano de lançamento é obrigatório")
    @Min(value = 1888, message = "Ano de lançamento deve ser maior que 1888")
    private Integer releaseYear;

    private String summary;

    private String posterUrl;

    @NotNull(message = "Data de exibição é obrigatória")
    private LocalDate screeningDate;

    @NotBlank(message = "O nome da sessão não pode ser vazio")
    private String session;
}
