package br.com.acervofilmesglobo.api_filmesglobo.controller;

import br.com.acervofilmesglobo.api_filmesglobo.dto.MovieResponseDTO;
import br.com.acervofilmesglobo.api_filmesglobo.dto.ScreeningLoadDTO;
import br.com.acervofilmesglobo.api_filmesglobo.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.acervofilmesglobo.api_filmesglobo.dto.ScreeningResponseDTO;
import jakarta.persistence.EntityNotFoundException;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;


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
        dto.setSession("Tela Quente");

        String requestBody = objectMapper.writeValueAsString(List.of(dto));

        doNothing().when(movieService).processScreeningLoad(any());

        mockMvc.perform(post("/api/movies/load")
                        .header("X-API-KEY", apiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        verify(movieService).processScreeningLoad(any());
    }

    @Test
    @DisplayName("Deve retornar status 401 Unauthorized quando a chave de API é inválida")
    void loadScreenings_withInvalidApiKey_shouldReturnUnauthorized() throws Exception {
        ScreeningLoadDTO dto = new ScreeningLoadDTO();
        dto.setOriginalTitle("Inception");
        dto.setPortugueseTitle("A Origem");
        dto.setReleaseYear(2010);
        dto.setScreeningDate(LocalDate.of(2025, 10, 11));
        dto.setSession("Tela Quente");
        String requestBody = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(post("/api/movies/load")
                        .header("X-API-KEY", "chave-errada")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());

        verify(movieService, never()).processScreeningLoad(any());
    }

    @Test
    @DisplayName("Deve retornar status 400 Bad Request quando o corpo da requisição é inválido")
    void loadScreenings_withInvalidBody_shouldReturnBadRequest() throws Exception {
        String requestBody = """
                [
                    {
                        "originalTitle": "Inception",
                        "portugueseTitle": "A Origem",
                        "releaseYear": 2010,
                        "screeningDate": "2025-10-11",
                        "session": ""
                    }
                ]
                """;

        mockMvc.perform(post("/api/movies/load")
                        .header("X-API-KEY", apiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(movieService, never()).processScreeningLoad(any());
    }

    @Test
    @DisplayName("Deve retornar status 200 OK e uma página de filmes ao listar todos")
    void listAll_shouldReturnOkAndPageOfMovies() throws Exception {
        // CENARIO
        MovieResponseDTO movieDto = new MovieResponseDTO();
        movieDto.setIdMovie(1L);
        movieDto.setPortugueseTitle("De Volta para o Futuro");
        movieDto.setPosterUrl("/api/movies/1/poster");
        movieDto.setScreeningsTotal(15);
        movieDto.setScreeningsUrl("/api/movies/1/screenings");

        Page<MovieResponseDTO> moviePage = new PageImpl<>(List.of(movieDto), PageRequest.of(0, 1), 1);

        when(movieService.findAll(any(Pageable.class))).thenReturn(moviePage);

        // AÇÃO
        mockMvc.perform(get("/api/movies")
                        .param("page", "0")
                        .param("size,", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.content[0].portugueseTitle").value("De Volta para o Futuro"));
    }

    @Test
    @DisplayName("Deve retornar 200 OK e a lista de exibições quando o filme existe")
    void listScreeningsByMovie_whenMovieExists_shouldReturnOkAndScreeningsList() throws Exception {
        ScreeningResponseDTO screening1 = new ScreeningResponseDTO();
        screening1.setPortugueseTitle("De Volta para o Futuro");
        screening1.setScreeningDate(LocalDate.of(2025, 10, 12));
        screening1.setSession("Sessão da Tarde");

        ScreeningResponseDTO screening2 = new ScreeningResponseDTO();
        screening2.setPortugueseTitle("De Volta para o Futuro");
        screening2.setScreeningDate(LocalDate.of(2024, 8, 15));
        screening2.setSession("Tela Quente");

        Long movieId = 1L;

        when(movieService.findScreeningsByMovieId(movieId)).thenReturn(List.of(screening1, screening2));

        mockMvc.perform(get("/api/movies/{id}/screenings", movieId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].session", is("Sessão da Tarde")))
                .andExpect(jsonPath("$[1].screeningDate", is("2024-08-15")));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando o filme não existe")
    void listScreeningsByMovie_whenMovieDoesNotExist_shouldReturnNotFound() throws Exception {

        Long nonExistentMovieId = 99L;

        when(movieService.findScreeningsByMovieId(nonExistentMovieId))
                .thenThrow(new EntityNotFoundException("Filme não encontrado"));

        mockMvc.perform(get("/api/movies/{id}/screenings", nonExistentMovieId))
                .andExpect(status().isNotFound());
    }
}