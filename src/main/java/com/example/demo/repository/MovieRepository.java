package com.example.demo.repository;

import com.example.demo.entity.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long> {

    Boolean existsByTitle(String title);

    Optional<Movie> findByTitle(String title);

}
