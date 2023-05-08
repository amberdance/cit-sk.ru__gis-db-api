package ru.hard2code.gisdbapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "information_systems")
public class InformationSystem extends AbstractEntity {


    public InformationSystem(String name) {
        this.name = name;
    }

    @Column(name = "name", nullable = false, unique = true)
    @NotNull
    private String name;

    @OneToMany(mappedBy = "informationSystem", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private List<Question> questions = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        InformationSystem that = (InformationSystem) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

}
