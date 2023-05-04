package ru.hard2code.GisDatabaseApi.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "chat_id", nullable = false, unique = true, length = 30)
    private String chatId;

    @ManyToOne
    @JoinColumn(name = "user_type_id", nullable = false)
    private UserType userType = new UserType();

    public User(String chatId) {
        this.chatId = chatId;
    }

    public User(String chatId, UserType userType) {
        this.chatId = chatId;
        this.userType = userType;
    }


}
