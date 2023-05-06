package ru.hard2code.gisdbapi.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
}