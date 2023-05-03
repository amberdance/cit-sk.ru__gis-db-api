package ru.hard2code.GisDatabaseApi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @Column(name = "chat_id", nullable = false, unique = true, length = 30)
    private final String chatId;

    @Column(name = "created_at")
    private final Date registeredAt = new Date();

    @ManyToOne
    @JoinColumn(name = "user_type_id")
    private final UserType userType;
}
