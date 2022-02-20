package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "award")
public class Award {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String year;
    private Integer ceremony;
    private String awardName;

    private Boolean winner;

    private Long actorId;
    // TODO add relation
    private Long filmId;
}
