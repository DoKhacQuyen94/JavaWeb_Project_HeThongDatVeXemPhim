package org.example.hethongquanlyvexemphim.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.hethongquanlyvexemphim.model.Seat;
import org.example.hethongquanlyvexemphim.model.User;
import org.example.hethongquanlyvexemphim.service.RoomService;
import org.example.hethongquanlyvexemphim.service.SeatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/seats")
@RequiredArgsConstructor
public class AdminSeatController {

    private final SeatService seatService;
    private final RoomService roomService;
    private final HttpSession session;

    // 1. LIỆT KÊ (Read)
    @GetMapping
    public String listSeats(Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("seats", seatService.findAll());
        return "admin/seats";
    }

    // 2. FORM THÊM MỚI (Create - UI)
    @GetMapping("/add")
    public String showAddForm(Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("seat", new Seat());
        model.addAttribute("rooms", roomService.findAll()); // Để chọn phòng cho ghế
        return "admin/seat-form";
    }

    // 3. FORM CHỈNH SỬA (Update - UI)
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }
        try {
            model.addAttribute("seat", seatService.findById(id));
            model.addAttribute("rooms", roomService.findAll());
            return "admin/seat-form";
        } catch (Exception e) {
            return "redirect:/admin/seats";
        }
    }

    // 4. LƯU DỮ LIỆU (Create/Update - Logic)
    @PostMapping("/save")
    public String saveSeat(@ModelAttribute("seat") Seat seat, RedirectAttributes ra) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }
        try {
            seatService.save(seat);
            ra.addFlashAttribute("message", "Lưu thông tin ghế thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/seats";
    }

    // 5. XÓA (Delete)
    @GetMapping("/delete/{id}")
    public String deleteSeat(@PathVariable("id") Integer id, RedirectAttributes ra) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }
        try {
            seatService.deleteById(id);
            ra.addFlashAttribute("message", "Xóa ghế thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Không thể xóa ghế này do đã có vé liên kết!");
        }
        return "redirect:/admin/seats";
    }
}