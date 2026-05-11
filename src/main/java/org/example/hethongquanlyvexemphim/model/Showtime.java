package org.example.hethongquanlyvexemphim.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "showtimes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Showtime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "showtime_id")
    private Integer showtimeId;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endTime;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
}