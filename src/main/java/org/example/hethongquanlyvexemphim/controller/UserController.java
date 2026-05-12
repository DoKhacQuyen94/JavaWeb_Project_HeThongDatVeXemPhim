package org.example.hethongquanlyvexemphim.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.hethongquanlyvexemphim.model.Movie;
import org.example.hethongquanlyvexemphim.model.Showtime;
import org.example.hethongquanlyvexemphim.model.User;
import org.example.hethongquanlyvexemphim.model.UserProfile;
import org.example.hethongquanlyvexemphim.repository.IUserRepository;
import org.example.hethongquanlyvexemphim.repository.MovieRepository;
import org.example.hethongquanlyvexemphim.service.IUserProfileService;
import org.example.hethongquanlyvexemphim.service.IUserService;
import org.example.hethongquanlyvexemphim.service.ShowtimeService;
import org.example.hethongquanlyvexemphim.service.impl.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final IUserProfileService userProfileService;
    private final ShowtimeService showtimeService;
    private final AdminService adminService;
    private final MovieRepository movieRepository;
    private final IUserRepository userRepository;
    private final HttpSession session;

    @GetMapping("/register")
    public String user(Model model) {
        model.addAttribute("register", new User());
        return "register";
    }
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        User user = (User) session.getAttribute("user");

        // --- LUỒNG CHO ADMIN ---
        if (user != null && "admin".equalsIgnoreCase(user.getRole())) {
            try {
                // Sử dụng các giá trị mặc định để tránh lỗi Null ở HTML
                Double revenue = adminService.getTotalRevenue();
                model.addAttribute("totalRevenue", revenue != null ? revenue : 0.0);
                model.addAttribute("totalTickets", adminService.getTotalTickets());
                model.addAttribute("totalMovies", movieRepository.count());
                model.addAttribute("totalUsers", userRepository.countByRole("user"));

                // Dữ liệu biểu đồ (Bọc trong list trống nếu lỗi để tránh vỡ Chart.js)
                List<String> labels = adminService.getRevenueLabels();
                List<Double> data = adminService.getRevenueData();

                model.addAttribute("revenueLabels", labels != null ? labels : new ArrayList<String>());
                model.addAttribute("revenueData", data != null ? data : new ArrayList<Double>());

            } catch (Exception e) {
                // Nếu lỗi SQL thống kê, Admin vẫn vào được trang nhưng thấy số 0
                model.addAttribute("error", "Lỗi tải thống kê: " + e.getMessage());
                model.addAttribute("totalRevenue", 0.0);
                model.addAttribute("revenueLabels", new ArrayList<String>());
                model.addAttribute("revenueData", new ArrayList<Double>());
            }
            return "admin/admin";
        }

        // --- LUỒNG CHO USER THƯỜNG ---
        else {
            // 1. Lấy suất chiếu (Tránh NullPointer nếu danh sách trống)
            List<Showtime> allShowtimes = showtimeService.getUpcomingShowtimes();
            if (allShowtimes == null) allShowtimes = new ArrayList<>();

            // 2. Gộp theo Phim
            Map<Movie, List<Showtime>> showtimesByMovie = allShowtimes.stream()
                    .filter(st -> st.getMovie() != null) // Bảo vệ chống dữ liệu rác
                    .collect(Collectors.groupingBy(Showtime::getMovie));

            model.addAttribute("showtimesByMovie", showtimesByMovie);

            // 3. Map hết vé (Sold out)
            Map<Integer, Boolean> soldOutMap = new HashMap<>();
            for (Showtime st : allShowtimes) {
                soldOutMap.put(st.getShowtimeId(), showtimeService.isSoldOut(st.getShowtimeId()));
            }
            model.addAttribute("soldOutMap", soldOutMap);

            return "user/home";
        }
    }
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("login", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("login") User userLogin,
                        RedirectAttributes redirectAttributes) {
        try{
            User ok = userService.login(userLogin.getEmail(), userLogin.getPassword());
            if (ok != null) {
                session.setAttribute("user", ok);
                // PHÂN QUYỀN TẠI ĐÂY
                if ("admin".equalsIgnoreCase(ok.getRole())) {
                    return "redirect:/home"; // Trang quản trị
                } else {
                    return "redirect:/home";
                }
            }
            redirectAttributes.addFlashAttribute("error", "Tài khoản mật khẩu sai");
            return "redirect:/login";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        // SỬA LỖI: Gọi service, bên trong service nên dùng repository.findById(userId)
        UserProfile profile = userProfileService.getUserProfile(user.getUserId());
        model.addAttribute("profile", profile);
        model.addAttribute("user", user);
        return "user/profile";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("register") User user, RedirectAttributes ra) {
        try {
            userService.registerUser(user);
            ra.addFlashAttribute("message", "Đăng ký thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/register";
    }

    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("logout","Bạn đã đăng xuất");
        return "redirect:/login";
    }
    @PostMapping("/profile/update")
    public String updateProfile(
            @RequestParam String fullName,
            @RequestParam String phone,
            @RequestParam(required = false) String oldPassword,
            @RequestParam(required = false) String newPassword,
            @RequestParam(required = false) String confirmPassword,
            RedirectAttributes ra) {

        // 1. Lấy User từ Session (Lưu ý: Luôn ép kiểu cẩn thận)
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        try {
            // 2. Gọi Service xử lý logic (Kiểm tra mật khẩu cũ, mã hóa mật khẩu mới...)
            // Lưu ý: userProfileService phải có hàm này
            userProfileService.updateUserProfile(user, fullName, phone, oldPassword, newPassword, confirmPassword);

            // 3. Cập nhật dữ liệu "nóng" vào Session để Navbar hiển thị đúng ngay lập tức
            // Giả sử User có quan hệ 1-1 với UserProfile
            if (user.getProfile() != null) {
                user.getProfile().setFullName(fullName);
                user.getProfile().setPhone(phone);
            }
            session.setAttribute("user", user);

            ra.addFlashAttribute("message", "Cập nhật hồ sơ thành công!");
        } catch (Exception e) {
            // 4. Bắt các lỗi như: Sai mật khẩu cũ, mật khẩu không khớp, mật khẩu quá ngắn...
            ra.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }

        return "redirect:/profile";
    }
}