package org.example.hethongquanlyvexemphim.service;

import org.example.hethongquanlyvexemphim.model.Showtime;
import java.util.List;

public interface ShowtimeService {
    List<Showtime> findAll();
    Showtime findById(Integer id);
    void save(Showtime showtime);
    void deleteById(Integer id);
    boolean isConflict(Showtime showtime);
    List<Showtime> getUpcomingShowtimes();
    boolean isSoldOut(Integer showtimeId);
}