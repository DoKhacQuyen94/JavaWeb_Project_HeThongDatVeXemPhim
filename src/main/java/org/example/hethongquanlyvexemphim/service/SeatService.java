package org.example.hethongquanlyvexemphim.service;
import org.example.hethongquanlyvexemphim.model.Seat;
import java.util.List;

public interface SeatService {
    List<Seat> findAll();
    Seat findById(Integer id);
    void save(Seat seat);
    void deleteById(Integer id);
}