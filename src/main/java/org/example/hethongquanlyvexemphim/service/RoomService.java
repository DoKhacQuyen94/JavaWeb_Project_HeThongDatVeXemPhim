package org.example.hethongquanlyvexemphim.service;

import org.example.hethongquanlyvexemphim.model.Room;
import java.util.List;

public interface RoomService {
    List<Room> findAll();
    Room findById(Integer id);
    void save(Room room);
    void deleteById(Integer id);
}