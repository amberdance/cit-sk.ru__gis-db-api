package ru.hard2code.GisDatabaseApi.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_types")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@ToString
public class UserType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private final Long id;

    @Column(name = "type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private final Type type;


    public enum Type {
        CITIZEN,
        EMPLOYEE;

    }


}

