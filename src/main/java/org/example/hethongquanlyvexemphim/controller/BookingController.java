package org.example.hethongquanlyvexemphim.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.hethongquanlyvexemphim.model.User;
import org.example.hethongquanlyvexemphim.repository.SeatRepository;
import org.example.hethongquanlyvexemphim.repository.ShowtimeRepository;
import org.example.hethongquanlyvexemphim.service.impl.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class BookingController {

    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final BookingService bookingService;

    /**
     * 1. Hiển thị trang chọn ghế
     */
    @GetMapping("/showtime/{id}/seats")
    public String showSeatSelectionForm(@PathVariable("id") Integer showtimeId,
                                        Model model,
                                        HttpSession session) {
        // Kiểm tra đăng nhập
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        // Lấy thông tin suất chiếu
        var showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy suất chiếu!"));

        // Gửi dữ liệu ra HTML
        model.addAttribute("showtime", showtime);

        // Lấy tất cả ghế của phòng chiếu (Dùng tên allSeats để khớp th:each)
        model.addAttribute("allSeats", seatRepository.findByRoom_RoomId(showtime.getRoom().getRoomId()));

        // Lấy danh sách ID ghế đã có người đặt (để khóa ghế)
        List<Integer> bookedSeatIds = bookingService.getBookedSeatIdsByShowtime(showtimeId);
        model.addAttribute("bookedSeatIds", bookedSeatIds);

        return "user/seat-selection";
    }

    /**
     * 2. Xử lý logic đặt vé khi nhấn nút Thanh Toán
     */
    @PostMapping("/booking/process")
    public String processBooking(@RequestParam("showtimeId") Integer showtimeId,
                                 @RequestParam(value = "seatIds", required = false) List<Integer> seatIds,
                                 HttpSession session,
                                 RedirectAttributes ra) {

        // Kiểm tra đăng nhập
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        // Kiểm tra nếu người dùng chưa chọn ghế nào
        if (seatIds == null || seatIds.isEmpty()) {
            ra.addFlashAttribute("error", "Vui lòng chọn ít nhất một chỗ ngồi!");
            return "redirect:/user/showtime/" + showtimeId + "/seats";
        }

        try {
            // Gọi Service xử lý (Kiểm tra trùng ghế & Lưu vào Database)
            bookingService.processBooking(user.getUserId(), showtimeId, seatIds);

            ra.addFlashAttribute("message", "Đặt vé thành công! Chúc bạn xem phim vui vẻ.");
            return "redirect:/user/booking/success";

        } catch (RuntimeException e) {
            // Bắt các lỗi như: "Ghế đã có người đặt trong lúc bạn thao tác"
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/user/showtime/" + showtimeId + "/seats";
        }
    }

    /**
     * 3. Hiển thị trang thông báo thành công
     */
    @GetMapping("/booking/success")
    public String bookingSuccessForm(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        return "user/booking-success";
    }
}