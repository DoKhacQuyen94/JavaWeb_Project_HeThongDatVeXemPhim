package org.example.hethongquanlyvexemphim.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.hethongquanlyvexemphim.model.Genre;
import org.example.hethongquanlyvexemphim.repository.IGenreRepository;
import org.example.hethongquanlyvexemphim.service.IGenreService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements IGenreService {
    private final IGenreRepository genreRepository;

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public Genre findById(Integer id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thể loại với ID: " + id));
    }

    @Override
    public void save(Genre genre) {
        genreRepository.save(genre);
    }

    @Override
    public void deleteById(Integer id) {
        genreRepository.deleteById(id);
    }
}