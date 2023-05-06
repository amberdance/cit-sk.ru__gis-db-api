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
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "answer", nullable = false)
    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    private String answer;

    @ManyToOne
    @JoinColumn(name = "information_system_id")
    private InformationSystem informationSystem;


    public Question(String label, String answer, InformationSystem informationSystem) {
        this.label = label;
        this.answer = answer;
        this.informationSystem = informationSystem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Question question = (Question) o;
        return getId() != null && Objects.equals(getId(), question.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}