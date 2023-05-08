package ru.hard2code.gisdbapi.model;

import jakarta.persistence.*;

@MappedSuperclass
public class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
