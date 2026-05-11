package org.example.hethongquanlyvexemphim.repository;

import org.example.hethongquanlyvexemphim.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    // Kiểm tra xem ghế đã có người đặt tại suất chiếu đó chưa
    boolean existsByShowtime_ShowtimeIdAndSeat_SeatId(Integer showtimeId, Integer seatId);

    // Lấy danh sách vé đã đặt của một suất chiếu (để hiển thị sơ đồ ghế)
    List<Ticket> findByShowtime_ShowtimeId(Integer showtimeId);

    // Lấy lịch sử vé của User, sắp xếp mới nhất lên đầu
    List<Ticket> findByBooking_User_UserIdOrderByBooking_CreatedAtDesc(Integer userId);

    // Tìm tất cả vé thuộc một hóa đơn (Booking)
    List<Ticket> findByBooking_BookingId(Integer bookingId);
    // Trong file TicketRepository.java
    long countByShowtime_ShowtimeIdAndStatusNot(Integer showtimeId, String status);
    boolean existsByShowtime_ShowtimeIdAndSeat_SeatIdAndStatusNotIgnoreCase(Integer showtimeId, Integer seatId, String status);
}