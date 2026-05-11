package org.example.hethongquanlyvexemphim.repository;

import org.example.hethongquanlyvexemphim.model.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Integer> {
    List<Showtime> findByRoom_RoomId(Integer roomId);

    // Lấy các suất chiếu trong tương lai
    List<Showtime> findByStartTimeAfterOrderByStartTimeAsc(LocalDateTime now);
}