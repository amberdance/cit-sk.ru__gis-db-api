package ru.hard2code.gisdbapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

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

    @Column(name = "username", nullable = false, unique = true)
    private String userName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @ManyToOne
    @JoinColumn(name = "user_type_id", nullable = false)
    private UserType userType = new UserType();

    public User(String chatId, String firstName, String userName) {
        this.chatId = chatId;
        this.firstName = firstName;
        this.userName = userName;
    }


    public User(String chatId, UserType userType) {
        this.chatId = chatId;
        this.userType = userType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return getId() != 0 && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
