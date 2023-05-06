package ru.hard2code.gisdbapi.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

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
        EMPLOYEE

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserType userType = (UserType) o;
        return getId() != 0 && Objects.equals(getId(), userType.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

