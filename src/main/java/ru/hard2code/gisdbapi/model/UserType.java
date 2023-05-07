package ru.hard2code.gisdbapi.model;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "user_types")
public class UserType extends AbstractEntity {


    @Column(name = "type", nullable = false, unique = true, length = 20)
    @Enumerated(EnumType.STRING)

    private Type type = Type.CITIZEN;

    public enum Type {
        CITIZEN("Гражданин"),
        GOVERNMENT_EMPLOYEE("Сотрудник ОГВ"),
        MUNICIPAL_EMPLOYEE("Сотрудник ОМСУ");

        private final String type;

        Type(String type) {
            this.type = type;
        }

        @JsonValue
        public String getValue() {
            return type;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserType userType = (UserType) o;
        return getId() != null && Objects.equals(getId(), userType.getId());
    }

}

