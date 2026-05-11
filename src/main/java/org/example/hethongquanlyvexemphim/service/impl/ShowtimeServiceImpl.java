package org.example.hethongquanlyvexemphim.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.hethongquanlyvexemphim.model.Showtime;
import org.example.hethongquanlyvexemphim.repository.ShowtimeRepository;
import org.example.hethongquanlyvexemphim.repository.TicketRepository;
import org.example.hethongquanlyvexemphim.service.ShowtimeService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowtimeServiceImpl implements ShowtimeService {
    private final ShowtimeRepository showtimeRepository;
    private final TicketRepository ticketRepository;
    @Override
    public List<Showtime> findAll() {
        return showtimeRepository.findAll();
    }

    @Override
    public Showtime findById(Integer id) {
        return showtimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy lịch chiếu với ID: " + id));
    }

    @Override
    public void save(Showtime showtime) {
        showtimeRepository.save(showtime);
    }

    @Override
    public void deleteById(Integer id) {
        showtimeRepository.deleteById(id);
    }

    @Override
    public boolean isConflict(Showtime newShowtime) {
        int cleaningTimeMinutes = 15;
        LocalDateTime newStart = newShowtime.getStartTime();
        LocalDateTime newEndWithCleaning = newShowtime.getEndTime().plusMinutes(cleaningTimeMinutes);

        List<Showtime> existingShowtimes = showtimeRepository.findByRoom_RoomId(newShowtime.getRoom().getRoomId());

        for (Showtime existing : existingShowtimes) {
            if (newShowtime.getShowtimeId() != null && newShowtime.getShowtimeId().equals(existing.getShowtimeId())) {
                continue;
            }
            LocalDateTime existStart = existing.getStartTime();
            LocalDateTime existEndWithCleaning = existing.getEndTime().plusMinutes(cleaningTimeMinutes);

            if (newStart.isBefore(existEndWithCleaning) && newEndWithCleaning.isAfter(existStart)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Showtime> getUpcomingShowtimes() {
        return showtimeRepository.findAll().stream()
                .filter(st -> st.getStartTime().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isSoldOut(Integer showtimeId) {
        Showtime st = showtimeRepository.findById(showtimeId).orElse(null);
        if (st == null) return true;

        // Tổng số ghế của phòng
        int totalSeats = st.getRoom().getSeats().size();

        // Đếm số vé đã xuất cho suất chiếu này (không tính vé đã bị cancel)
        long bookedSeats = ticketRepository.countByShowtime_ShowtimeIdAndStatusNot(showtimeId, "cancel");

        return bookedSeats >= totalSeats;
    }

    
}