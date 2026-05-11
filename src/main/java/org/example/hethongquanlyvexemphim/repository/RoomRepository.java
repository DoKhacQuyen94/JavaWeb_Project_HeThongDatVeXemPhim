package org.example.hethongquanlyvexemphim.repository;

import org.example.hethongquanlyvexemphim.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
}