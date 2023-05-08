package ru.hard2code.gisdbapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "user_types")
public class UserType extends AbstractEntity {


    @Column(name = "name", nullable = false, unique = true, length = 100)
    @NotNull
    private String name = Type.CITIZEN.getValue();

    @OneToMany(mappedBy = "userType")
    @ToString.Exclude
    @JsonIgnore
    private List<User> questions = new ArrayList<>();

    public UserType(String name) {
        this.name = name;
    }


    public enum Type {
        CITIZEN("Гражданин"),
        GOVERNMENT_EMPLOYEE("Сотрудник ОГВ"),
        MUNICIPAL_EMPLOYEE("Сотрудник ОМСУ");

        private final String type;

        Type(String type) {
            this.type = type;
        }

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

