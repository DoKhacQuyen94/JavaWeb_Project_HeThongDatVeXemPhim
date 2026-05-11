package org.example.hethongquanlyvexemphim.repository;

import jakarta.transaction.Transactional;
import org.example.hethongquanlyvexemphim.model.Movie;
import org.example.hethongquanlyvexemphim.model.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovieGenreRepository extends JpaRepository<MovieGenre, Integer> {
    List<MovieGenre> findByMovie(Movie movie);

    @Modifying // Bắt buộc cho lệnh xóa
    @Transactional
    void deleteByMovie(Movie movie); // Dùng để xóa các thể loại cũ khi sửa phim
}