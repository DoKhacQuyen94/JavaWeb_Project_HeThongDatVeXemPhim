package org.example.hethongquanlyvexemphim.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "genres")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private Integer genreId;

    private String name;

    @OneToMany(mappedBy = "genre")
    private List<MovieGenre> movieGenres;
}