package org.example.hethongquanlyvexemphim.repository;

import org.example.hethongquanlyvexemphim.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    // 1. Tính tổng doanh thu: Dùng cột 'price' trong bảng 'tickets'
    @Query(value = "SELECT SUM(t.price) FROM tickets t WHERE t.status = 'paid'", nativeQuery = true)
    Double getTotalRevenue();

    // 2. Thống kê 7 ngày: Dùng cột 'created_at' trong bảng 'bookings'
    @Query(value = "SELECT DATE(b.created_at) as date, SUM(t.price) as amount " +
            "FROM bookings b " +
            "JOIN tickets t ON b.booking_id = t.booking_id " +
            "WHERE t.status = 'paid' AND b.created_at >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) " +
            "GROUP BY DATE(b.created_at) " +
            "ORDER BY date ASC", nativeQuery = true)
    List<Object[]> getRevenueByDay();
}