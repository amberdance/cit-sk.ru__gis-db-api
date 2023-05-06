package ru.hard2code.GisDatabaseApi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "information_systems")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InformationSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;


    @Column(name = "name", nullable = false, unique = true)
    private String name;


    public InformationSystem(String name) {
        this.name = name;
    }
}
