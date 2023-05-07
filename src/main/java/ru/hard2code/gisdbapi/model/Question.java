package ru.hard2code.gisdbapi.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "questions")
public class Question extends AbstractEntity {


    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "answer", nullable = false)
    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    private String answer;

    @ManyToOne
    @JoinColumn(name = "information_system_id")
    private InformationSystem informationSystem;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Question question = (Question) o;
        return getId() != null && Objects.equals(getId(), question.getId());
    }

}