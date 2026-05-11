package org.example.hethongquanlyvexemphim.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "rooms")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Integer roomId;
    private String roomName;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<Seat> seats;
}