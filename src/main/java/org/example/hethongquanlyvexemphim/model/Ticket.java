package org.example.hethongquanlyvexemphim.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tickets")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Integer ticketId;

    @Column(name = "price")
    private Double price; // Đã sửa tên từ total_price

    private String status; // paid, cancel

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "showtime_id")
    private Showtime showtime;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;
}