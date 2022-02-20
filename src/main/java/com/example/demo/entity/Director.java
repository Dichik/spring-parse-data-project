package com.example.demo.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "director")
public class Director {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String secondName;

    public Director(String firstName, String secondName) {
        this.firstName = firstName;
        this.secondName = secondName;
    }
}
