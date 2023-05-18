package ru.hard2code.gisdbapi.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "organizations")
public class Organization extends AbstractEntity {

    @Column(name = "name", nullable = false, unique = true)
    @NotNull
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "is_government", nullable = false)
    @NotNull
    private boolean isGovernment = false;

    public Organization(String name, String address) {
        this.name = name;
        this.address = address;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Organization that = (Organization) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

}
