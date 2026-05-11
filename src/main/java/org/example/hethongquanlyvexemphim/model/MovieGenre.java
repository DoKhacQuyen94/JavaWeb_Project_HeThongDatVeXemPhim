package org.example.hethongquanlyvexemphim.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movie_genres")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;
}