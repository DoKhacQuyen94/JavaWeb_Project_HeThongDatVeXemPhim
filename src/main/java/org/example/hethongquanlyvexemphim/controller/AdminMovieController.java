package org.example.hethongquanlyvexemphim.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.hethongquanlyvexemphim.model.Movie;
import org.example.hethongquanlyvexemphim.model.User;
import org.example.hethongquanlyvexemphim.service.IGenreService;
import org.example.hethongquanlyvexemphim.service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/movies")
@RequiredArgsConstructor
public class AdminMovieController {
    private final MovieService movieService;
    private final IGenreService genreService;
    private final HttpSession session;

    @GetMapping
    public String listMovies(Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) return "redirect:/login";

        model.addAttribute("movies", movieService.findAll());
        return "admin/movies";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        model.addAttribute("movie", new Movie());
        model.addAttribute("allGenres", genreService.findAll());
        return "admin/movie-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        model.addAttribute("movie", movieService.findById(id));
        model.addAttribute("allGenres", genreService.findAll());
        model.addAttribute("selectedGenres", movieService.getGenreIdsByMovieId(id));
        return "admin/movie-form";
    }

    @PostMapping("/save")
    public String saveMovie(@ModelAttribute("movie") Movie movie,
                            @RequestParam(value = "genreIds", required = false) List<Integer> genreIds,
                            RedirectAttributes redirectAttributes) {
        try {
            if(movie.getPosterUrl() == null || movie.getPosterUrl().equals("")) {
                movie.setPosterUrl("https://e7.pngegg.com/pngimages/373/244/png-clipart-film-cinematography-others-text-logo-thumbnail.png");
            }
            movieService.saveMovieWithGenres(movie, genreIds);
            redirectAttributes.addFlashAttribute("message", "Cập nhật phim thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi lưu phim: " + e.getMessage());
        }
        return "redirect:/admin/movies";
    }

    @GetMapping("/delete/{id}")
    public String deleteMovie(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            movieService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Xóa phim thành công!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Không thể xóa phim (có thể đã có lịch chiếu).");
        }
        return "redirect:/admin/movies";
    }
}