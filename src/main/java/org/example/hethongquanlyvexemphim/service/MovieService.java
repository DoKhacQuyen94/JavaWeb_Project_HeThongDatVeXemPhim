package org.example.hethongquanlyvexemphim.service;

import org.example.hethongquanlyvexemphim.model.Movie;
import java.util.List;

public interface MovieService {
    List<Movie> findAll();
    Movie findById(Integer id);
    void saveMovieWithGenres(Movie movie, List<Integer> genreIds);
    void deleteById(Integer id);
    List<Integer> getGenreIdsByMovieId(Integer movieId);
}