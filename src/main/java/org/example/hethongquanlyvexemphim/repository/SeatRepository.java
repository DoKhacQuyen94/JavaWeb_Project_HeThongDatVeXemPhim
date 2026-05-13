package org.example.hethongquanlyvexemphim.repository;

import org.example.hethongquanlyvexemphim.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {
    List<Seat> findByRoom_RoomId(Integer roomId);
}