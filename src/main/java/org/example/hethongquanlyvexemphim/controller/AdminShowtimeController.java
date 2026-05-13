package org.example.hethongquanlyvexemphim.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.hethongquanlyvexemphim.model.Showtime;
import org.example.hethongquanlyvexemphim.service.MovieService;
import org.example.hethongquanlyvexemphim.service.RoomService;
import org.example.hethongquanlyvexemphim.service.ShowtimeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/showtimes")
@RequiredArgsConstructor
public class AdminShowtimeController {
    private final ShowtimeService showtimeService;
    private final RoomService roomService;
    private final MovieService movieService;

    // 1. LIỆT KÊ DANH SÁCH (Read)
    @GetMapping
    public String listShowtimes(Model model) {
        model.addAttribute("showtimes", showtimeService.findAll());
        return "admin/showtimes";
    }

    // 2. FORM THÊM MỚI (Create - UI)
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("showtime", new Showtime());
        model.addAttribute("movies", movieService.findAll()); // Để chọn phim
        model.addAttribute("rooms", roomService.findAll());   // Để chọn phòng
        return "admin/showtime-form";
    }

    // 3. FORM CHỈNH SỬA (Update - UI)
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        try {
            Showtime showtime = showtimeService.findById(id);
            model.addAttribute("showtime", showtime);
            model.addAttribute("movies", movieService.findAll());
            model.addAttribute("rooms", roomService.findAll());
            return "admin/showtime-form";
        } catch (Exception e) {
            return "redirect:/admin/showtimes";
        }
    }

    // 4. LƯU DỮ LIỆU (Create/Update - Logic)
    @PostMapping("/save")
    public String saveShowtime(@ModelAttribute("showtime") Showtime showtime, RedirectAttributes ra) {
        // Kiểm tra logic thời gian cơ bản
        if (showtime.getEndTime().isBefore(showtime.getStartTime())) {
            ra.addFlashAttribute("error", "Giờ kết thúc phải sau giờ bắt đầu!");
            return "redirect:/admin/showtimes/add";
        }

        // Kiểm tra xung đột (đã bao gồm 15p dọn phòng trong Service)
        if (showtimeService.isConflict(showtime)) {
            ra.addFlashAttribute("error", "Xung đột lịch chiếu! Phòng này đã có lịch trong khoảng thời gian trên.");
            // Nếu là edit thì trả về link edit, nếu add thì trả về link add
            String redirectUrl = (showtime.getShowtimeId() != null) ? "/edit/" + showtime.getShowtimeId() : "/add";
            return "redirect:/admin/showtimes" + redirectUrl;
        }

        showtimeService.save(showtime);
        ra.addFlashAttribute("message", "Lưu lịch chiếu thành công!");
        return "redirect:/admin/showtimes";
    }

    // 5. XÓA (Delete)
    @GetMapping("/delete/{id}")
    public String deleteShowtime(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            showtimeService.deleteById(id);
            ra.addFlashAttribute("message", "Đã xóa lịch chiếu thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Không thể xóa lịch chiếu này vì đã có người đặt vé!");
        }
        return "redirect:/admin/showtimes";
    }
}