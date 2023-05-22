package ru.hard2code.gisdbapi.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.util.Objects;


@Entity
@Table(name = "organizations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 128)
    @NotNull
    private String name;

    @Column(name = "address", length = 128)
    private String address;

    @Column(name = "is_government", nullable = false)
    @Builder.Default
    private boolean isGovernment = true;

    public Organization(@NotNull String name, String address) {
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
