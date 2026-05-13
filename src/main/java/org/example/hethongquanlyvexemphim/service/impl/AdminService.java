package org.example.hethongquanlyvexemphim.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.hethongquanlyvexemphim.repository.BookingRepository;
import org.example.hethongquanlyvexemphim.repository.IUserRepository;
import org.example.hethongquanlyvexemphim.repository.MovieRepository;
import org.example.hethongquanlyvexemphim.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;

    public Double getTotalRevenue() {
        Double rev = bookingRepository.getTotalRevenue();
        return rev != null ? rev : 0.0;
    }
    public long getTotalTickets() {
        // Trả về tổng số vé không bị hủy
        return ticketRepository.countByStatusNotIgnoreCase("cancel");
    }
}
