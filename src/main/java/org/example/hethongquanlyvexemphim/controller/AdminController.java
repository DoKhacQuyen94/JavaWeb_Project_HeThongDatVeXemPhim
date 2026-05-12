package org.example.hethongquanlyvexemphim.controller; // Điều chỉnh package nếu cần

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.hethongquanlyvexemphim.model.Genre;
import org.example.hethongquanlyvexemphim.model.User;
import org.example.hethongquanlyvexemphim.service.IGenreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/genres")
@RequiredArgsConstructor

public class AdminController {

    private final IGenreService genreService;
    private final HttpSession session;
    @GetMapping
    public String listGenres(Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("genres", genreService.findAll());
        return "admin/genres";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("genre", new Genre());
        return "admin/genre-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Genre genre = genreService.findById(id);
        model.addAttribute("genre", genre);
        return "admin/genre-form";
    }

    @PostMapping("/save")
    public String saveGenre(@ModelAttribute("genre") Genre genre, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        genreService.save(genre);
        redirectAttributes.addFlashAttribute("message", "Lưu thể loại thành công!");
        return "redirect:/admin/genres";
    }

    @GetMapping("/delete/{id}")
    public String deleteGenre(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        try {
            genreService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Xóa thể loại thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa thể loại này! Có thể đang có phim thuộc thể loại này.");
        }
        return "redirect:/admin/genres";
    }


}