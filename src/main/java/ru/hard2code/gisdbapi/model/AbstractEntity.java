package ru.hard2code.gisdbapi.model;

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
