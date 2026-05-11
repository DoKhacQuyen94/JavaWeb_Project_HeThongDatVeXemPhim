package org.example.hethongquanlyvexemphim.repository;

import org.example.hethongquanlyvexemphim.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
}