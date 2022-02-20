package com.example.demo.repository;

import com.example.demo.entity.Director;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.awt.datatransfer.StringSelection;
import java.util.Optional;

@Repository
public interface DirectorRepository extends CrudRepository<Director, Long> {

    Boolean existsDirectorByFirstNameAndSecondName(String firstName, String secondName);

    Optional<Director> findDirectorByFirstNameAndSecondName(String firstName, String secondName);

}
