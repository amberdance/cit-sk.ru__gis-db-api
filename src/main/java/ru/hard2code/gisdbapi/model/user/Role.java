package ru.hard2code.gisdbapi.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;
import ru.hard2code.gisdbapi.model.AbstractEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "user_roles")
public class Role extends AbstractEntity {

    @Column(name = "name", nullable = false, unique = true, length = 100)
    @NotNull
    private String name;

    @OneToMany(mappedBy = "role")
    @ToString.Exclude
    @JsonIgnore
    private List<User> users = new ArrayList<>();

    public Role(String name) {
        this.name = name;
    }


    public enum Type {
        CITIZEN("Гражданин"),
        STAFF("Куратор вопросов"),
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
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Role role = (Role) o;
        return getId() != null && Objects.equals(getId(), role.getId());
    }

}
