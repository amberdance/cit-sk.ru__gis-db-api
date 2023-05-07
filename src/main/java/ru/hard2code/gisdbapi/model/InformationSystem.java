package ru.hard2code.gisdbapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "information_systems")
public class InformationSystem extends AbstractEntity {


    @Column(name = "name", nullable = false, unique = true)
    private String name;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        InformationSystem that = (InformationSystem) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

}
