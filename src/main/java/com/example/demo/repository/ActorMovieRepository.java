package com.example.demo.repository;

import com.example.demo.entity.ActorMovie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorMovieRepository extends CrudRepository<ActorMovie, Long> {
}
