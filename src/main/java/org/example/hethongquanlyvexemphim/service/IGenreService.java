package org.example.hethongquanlyvexemphim.service;

import org.example.hethongquanlyvexemphim.model.Genre;
import java.util.List;

public interface IGenreService {
    List<Genre> findAll();
    Genre findById(Integer id);
    void save(Genre genre);
    void deleteById(Integer id);
}