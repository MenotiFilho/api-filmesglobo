package br.com.acervofilmesglobo.api_filmesglobo.controller;

import br.com.acervofilmesglobo.api_filmesglobo.dto.ScreeningLoadDTO;
import br.com.acervofilmesglobo.api_filmesglobo.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(MovieController.class)
@DisplayName("Testes para o Movie Controller")
public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MovieService movieService;

    @Value("${api.security.key}")
    private String apiKey;

    @Test
    @DisplayName("Deve retornar status 200 OK quando a chave de API e o corpo da requisição são válidos")
    void loadScreenings_withValidKeyAndBody_shouldReturnOk() throws Exception {
        ScreeningLoadDTO dto = new ScreeningLoadDTO();
        dto.setOriginalTitle("Inception");
        dto.setPortugueseTitle("A Origem");
        dto.setReleaseYear(2010);
        dto.setScreeningDate(LocalDate.of(2025, 10, 11));

        String requestBody = objectMapper.writeValueAsString(List.of(dto));

        doNothing().when(movieService).processScreeningLoad(any());

        mockMvc.perform(post("/api/movies/load")
                        .header("X-API-KEY", apiKey)
                        .contentType(MediaType.APPLICATION_JSON) // Set the content type of the request
                        .content(requestBody))
                .andExpect(status().isOk());

        verify(movieService).processScreeningLoad(any());
    }


    // Todo: teste apikey invalida e teste body invalido
}
