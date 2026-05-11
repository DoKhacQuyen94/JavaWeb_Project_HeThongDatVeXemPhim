package org.example.hethongquanlyvexemphim.service.impl;
import lombok.RequiredArgsConstructor;
import org.example.hethongquanlyvexemphim.model.Seat;
import org.example.hethongquanlyvexemphim.repository.SeatRepository;
import org.example.hethongquanlyvexemphim.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {
     private  final SeatRepository seatRepository;

    @Override public List<Seat> findAll() { return seatRepository.findAll(); }
    @Override public Seat findById(Integer id) {
        return seatRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy ghế"));
    }
    @Override public void save(Seat seat) { seatRepository.save(seat); }
    @Override public void deleteById(Integer id) { seatRepository.deleteById(id); }
}