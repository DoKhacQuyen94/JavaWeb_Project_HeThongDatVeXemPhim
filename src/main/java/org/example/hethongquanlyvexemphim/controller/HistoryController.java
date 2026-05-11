package org.example.hethongquanlyvexemphim.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.hethongquanlyvexemphim.model.Booking;
import org.example.hethongquanlyvexemphim.model.Ticket;
import org.example.hethongquanlyvexemphim.model.User;
import org.example.hethongquanlyvexemphim.repository.TicketRepository;
import org.example.hethongquanlyvexemphim.service.impl.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user/history")
@RequiredArgsConstructor
public class HistoryController {

    private final TicketRepository ticketRepository;
    private final BookingService bookingService;

    @GetMapping
    public String viewHistory(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        List<Ticket> userTickets = ticketRepository.findByBooking_User_UserIdOrderByBooking_CreatedAtDesc(user.getUserId());
        Map<Booking, List<Ticket>> ticketsByBooking = userTickets.stream()
                .collect(Collectors.groupingBy(Ticket::getBooking));

        model.addAttribute("ticketsByBooking", ticketsByBooking);
        return "user/history";
    }

    @PostMapping("/cancel")
    public String cancelBooking(@RequestParam("bookingId") Integer bookingId,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        try {
            bookingService.cancelBooking(bookingId, user.getUserId());
            redirectAttributes.addFlashAttribute("message", "Đã hủy đơn hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/user/history";
    }
}