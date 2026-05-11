package org.example.hethongquanlyvexemphim.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.hethongquanlyvexemphim.model.*;
import org.example.hethongquanlyvexemphim.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;

    @Transactional(rollbackOn = Exception.class)
    public Booking processBooking(Integer userId, Integer showtimeId, List<Integer> seatIds) {
        // 1. Kiểm tra tính hợp lệ của tất cả các ghế trước khi thực hiện lưu
        for (Integer seatId : seatIds) {
            // Chỉ chặn nếu ghế đó đang có vé với trạng thái KHÁC 'cancel'
            boolean isTaken = ticketRepository.existsByShowtime_ShowtimeIdAndSeat_SeatIdAndStatusNotIgnoreCase(
                    showtimeId, seatId, "cancel");

            if (isTaken) {
                String seatName = seatRepository.findById(seatId)
                        .map(Seat::getSeatName)
                        .orElse("ID: " + seatId);
                throw new RuntimeException("Ghế " + seatName + " đã có người đặt trong lúc bạn thao tác!");
            }
        }

        // 2. Tìm thông tin Suất chiếu và Người dùng
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy suất chiếu!"));

        User user = new User();
        user.setUserId(userId);

        // 3. Tạo và lưu mới đối tượng Booking (Hóa đơn)
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShowtime(showtime);
        booking.setCreatedAt(LocalDateTime.now());
        Booking savedBooking = bookingRepository.save(booking);

        Double ticketPrice = showtime.getPrice();

        // 4. Xử lý lưu từng vé (Ticket)
        for (Integer seatId : seatIds) {
            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new IllegalArgumentException("Ghế không hợp lệ!"));

            // Lấy danh sách vé đã tồn tại cho cặp (showtime, seat)
            // Vì có Unique Constraint nên danh sách này chỉ có tối đa 1 phần tử
            List<Ticket> existingTickets = ticketRepository.findByShowtime_ShowtimeId(showtimeId)
                    .stream()
                    .filter(t -> t.getSeat().getSeatId().equals(seatId))
                    .collect(Collectors.toList());

            Ticket ticket;
            if (!existingTickets.isEmpty()) {
                // Nếu đã tồn tại bản ghi (chắc chắn là 'cancel' vì đã check ở bước 1), thực hiện cập nhật
                ticket = existingTickets.get(0);
            } else {
                // Nếu chưa từng có bản ghi nào thì mới tạo mới
                ticket = new Ticket();
            }

            // Cập nhật thông tin vé mới/cập nhật
            ticket.setBooking(savedBooking);
            ticket.setShowtime(showtime);
            ticket.setSeat(seat);
            ticket.setPrice(ticketPrice);
            ticket.setStatus("paid"); // Chuyển từ 'cancel' sang 'paid' hoặc thiết lập mới

            ticketRepository.save(ticket);
        }

        return savedBooking;
    }

    @Transactional(rollbackOn = Exception.class)
    public void cancelBooking(Integer bookingId, Integer currentUserId) {
        List<Ticket> tickets = ticketRepository.findByBooking_BookingId(bookingId);
        if (tickets.isEmpty()) throw new RuntimeException("Không tìm thấy đơn hàng!");

        if (!tickets.get(0).getBooking().getUser().getUserId().equals(currentUserId)) {
            throw new RuntimeException("Bạn không có quyền hủy đơn hàng này!");
        }

        Showtime showtime = tickets.get(0).getShowtime();
        if (LocalDateTime.now().plusHours(24).isAfter(showtime.getStartTime())) {
            throw new RuntimeException("Chỉ được hủy vé trước giờ chiếu 24 giờ!");
        }

        for (Ticket ticket : tickets) {
            ticket.setStatus("cancel"); // Cập nhật trạng thái vé thành cancel
        }
        ticketRepository.saveAll(tickets);
    }
    public List<Integer> getBookedSeatIdsByShowtime(Integer showtimeId) {
        // Giả sử bạn có TicketRepository
        return ticketRepository.findByShowtime_ShowtimeId(showtimeId)
                .stream()
                .filter(t -> !"cancel".equalsIgnoreCase(t.getStatus())) // Không tính vé đã hủy
                .map(t -> t.getSeat().getSeatId())
                .collect(Collectors.toList());
    }
}