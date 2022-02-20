package com.example.demo.repository;

import com.example.demo.entity.Actor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActorRepository extends CrudRepository<Actor, Long> {

    Optional<Actor> findByFirstNameAndSecondName(String firstName, String secondName);

    Boolean existsByFirstNameAndSecondName(String firstName, String secondName);

}
