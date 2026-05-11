package org.example.hethongquanlyvexemphim.repository;

import org.example.hethongquanlyvexemphim.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
}