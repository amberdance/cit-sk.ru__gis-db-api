package ru.hard2code.GisDatabaseApi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
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

    @Column(name = "chat_id", nullable = false, unique = true, length = 50)
    private String chatId;

    @Column(name = "email", unique = true, length = 50)
    @Email(message = "Invalid email")
    private String email;

    @Column(name = "phone", unique = true, length = 12)
    @Pattern(regexp = "^(\\+7|7|8)?(9){1}?[\\d]{9}")
    private String phone;

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
