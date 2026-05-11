package org.example.hethongquanlyvexemphim.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.hethongquanlyvexemphim.model.Genre;
import org.example.hethongquanlyvexemphim.model.Movie;
import org.example.hethongquanlyvexemphim.model.MovieGenre;
import org.example.hethongquanlyvexemphim.repository.IGenreRepository;
import org.example.hethongquanlyvexemphim.repository.MovieGenreRepository;
import org.example.hethongquanlyvexemphim.repository.MovieRepository;
import org.example.hethongquanlyvexemphim.service.MovieService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieGenreRepository movieGenreRepository;
    private final IGenreRepository genreRepository;

    @Override
    @Transactional
    public void saveMovieWithGenres(Movie movie, List<Integer> genreIds) {
        Movie savedMovie = movieRepository.save(movie);

        // Làm sạch thể loại cũ nếu cập nhật
        if (movie.getMovieId() != null) {
            movieGenreRepository.deleteByMovie(savedMovie);
        }

        // Thêm thể loại mới
        if (genreIds != null) {
            genreIds.forEach(id -> {
                Genre genre = genreRepository.findById(id).orElse(null);
                if (genre != null) {
                    MovieGenre mg = new MovieGenre();
                    mg.setMovie(savedMovie);
                    mg.setGenre(genre);
                    movieGenreRepository.save(mg);
                }
            });
        }
    }

    @Override
    public List<Integer> getGenreIdsByMovieId(Integer movieId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow();
        return movieGenreRepository.findByMovie(movie).stream()
                .map(mg -> mg.getGenre().getGenreId())
                .collect(Collectors.toList());
    }

    @Override public List<Movie> findAll() { return movieRepository.findAll(); }
    @Override public Movie findById(Integer id) { return movieRepository.findById(id).orElseThrow(); }
    @Override @Transactional public void deleteById(Integer id) {

            Movie movie = findById(id);
            if (movieGenreRepository.existsById(movie.getMovieId())) {
                movieGenreRepository.deleteById(movie.getMovieId());
            }
            movieRepository.deleteById(id);

    }
}