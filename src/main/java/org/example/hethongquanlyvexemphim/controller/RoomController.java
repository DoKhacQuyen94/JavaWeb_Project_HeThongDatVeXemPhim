package org.example.hethongquanlyvexemphim.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.hethongquanlyvexemphim.model.Room;
import org.example.hethongquanlyvexemphim.model.User;
import org.example.hethongquanlyvexemphim.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final HttpSession session;

    // 1. LIỆT KÊ (Read)
    @GetMapping
    public String listRooms(Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("rooms", roomService.findAll());
        return "admin/rooms";
    }

    // 2. FORM THÊM MỚI (Create - UI)
    @GetMapping("/add")
    public String showAddForm(Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("room", new Room());
        return "admin/room-form";
    }

    // 3. FORM CHỈNH SỬA (Update - UI)
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }
        try {
            Room room = roomService.findById(id);
            model.addAttribute("room", room);
            return "admin/room-form";
        } catch (Exception e) {
            return "redirect:/admin/rooms";
        }
    }

    // 4. LƯU DỮ LIỆU (Create/Update - Logic)
    @PostMapping("/save")
    public String saveRoom(@ModelAttribute("room") Room room, RedirectAttributes ra) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }
        try {
            roomService.save(room);
            ra.addFlashAttribute("message", "Lưu phòng chiếu thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/rooms";
    }

    // 5. XÓA (Delete)
    @GetMapping("/delete/{id}")
    public String deleteRoom(@PathVariable("id") Integer id, RedirectAttributes ra) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }
        try {
            roomService.deleteById(id);
            ra.addFlashAttribute("message", "Xóa phòng chiếu thành công!");
        } catch (Exception e) {
            // Lỗi này thường do ràng buộc khóa ngoại (phòng đã có ghế hoặc lịch chiếu)
            ra.addFlashAttribute("error", "Không thể xóa phòng này do đã có dữ liệu liên kết (Ghế hoặc Lịch chiếu)!");
        }
        return "redirect:/admin/rooms";
    }
}