package org.example.hethongquanlyvexemphim.service.impl;

import org.example.hethongquanlyvexemphim.model.Room;
import org.example.hethongquanlyvexemphim.repository.RoomRepository;
import org.example.hethongquanlyvexemphim.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    @Override
    public Room findById(Integer id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng chiếu với ID: " + id));
    }

    @Override
    public void save(Room room) {
        roomRepository.save(room);
    }

    @Override
    public void deleteById(Integer id) {
        roomRepository.deleteById(id);
    }
}