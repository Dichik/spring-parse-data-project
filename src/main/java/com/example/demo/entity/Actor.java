package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "actor",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"firstName", "secondName"})})
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String secondName;

    public Actor(String firstName, String secondName) {
        this.firstName = firstName;
        this.secondName = secondName;
    }
}
