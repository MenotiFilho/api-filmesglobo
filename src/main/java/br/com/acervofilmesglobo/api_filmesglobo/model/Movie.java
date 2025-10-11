package br.com.acervofilmesglobo.api_filmesglobo.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Formula;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "movie", indexes = {
        @Index(name = "idx_movie_original_title", columnList = "originalTitle"),
        @Index(name = "idx_movie_portuguese_title", columnList = "portugueseTitle"),
})
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMovie;

    @Column(unique = true, nullable = false, length = 255)
    private String originalTitle;

    @Column(nullable = false, length = 255)
    private String portugueseTitle;

    private Integer releaseYear;

    @Column(length = 2000)
    private String summary;

    @Column(length = 512)
    private String posterUrl;

    @OneToMany(
            mappedBy = "movie",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Screening> screeningHistory = new ArrayList<>();

    @Formula("(SELECT COUNT(s.id_screening) FROM screening s WHERE s.movie_id = id_movie)")
    private int screeningsTotal;
}