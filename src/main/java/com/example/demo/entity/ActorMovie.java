package com.example.demo.entity;

import com.fasterxml.jackson.databind.node.LongNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "actor_movie")
public class ActorMovie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long actorId;
    private Long movieId;
    private String year;
    private UUID ratingId;
}
