package org.example.hethongquanlyvexemphim.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "movies")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Integer movieId;

    private String title;

    @Column(columnDefinition = "TEXT") // Cho phép mô tả dài
    private String description;

    private Integer duration;
    private LocalDate releaseDate;

    private String posterUrl;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;

    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private List<MovieGenre> movieGenres; // Tên biến phải khớp với HTML
}