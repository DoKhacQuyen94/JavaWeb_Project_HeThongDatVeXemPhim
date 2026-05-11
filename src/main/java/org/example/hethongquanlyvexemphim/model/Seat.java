package org.example.hethongquanlyvexemphim.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seats")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Integer seatId;
    private String seatName;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
}