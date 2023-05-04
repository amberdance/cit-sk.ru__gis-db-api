package ru.hard2code.GisDatabaseApi.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_types")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "type", nullable = false, unique = true, length = 20)
    @Enumerated(EnumType.STRING)
    private Type type = Type.CITIZEN;

    public UserType(Type type) {
        this.type = type;
    }

    public enum Type {
        CITIZEN,
        EMPLOYEE;

    }


}

