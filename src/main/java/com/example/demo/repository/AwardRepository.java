package com.example.demo.repository;

import com.example.demo.entity.Award;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AwardRepository extends CrudRepository<Award, Long> {
}
