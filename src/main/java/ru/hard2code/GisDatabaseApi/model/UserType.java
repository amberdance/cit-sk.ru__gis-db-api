package ru.hard2code.GisDatabaseApi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_types")
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class UserType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @Column(name = "type", nullable = false, length = 20)
    private final String type = "employee";


}

