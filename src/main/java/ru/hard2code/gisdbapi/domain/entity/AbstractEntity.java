package ru.hard2code.gisdbapi.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

@MappedSuperclass
public class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Getter
    private Long id;


}
