package org.example.hethongquanlyvexemphim.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer bookingId;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "showtime_id")
    private Showtime showtime;
}