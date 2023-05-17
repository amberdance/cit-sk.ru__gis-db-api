package ru.hard2code.gisdbapi.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.Hibernate;
import ru.hard2code.gisdbapi.model.AbstractEntity;
import ru.hard2code.gisdbapi.model.Message;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User extends AbstractEntity {

    @Column(name = "chat_id", nullable = false, unique = true, length = 10)
    @Pattern(regexp = "^\\d{9,10}$")
    @NotNull
    private String chatId;

    @Column(name = "username", nullable = false, unique = true)
    @NotNull
    private String userName;

    @Column(name = "email", unique = true, length = 50)
    @Email
    private String email;

    @Column(name = "phone", unique = true, length = 12)
    @Pattern(regexp = "^(\\+7|7|8)?(9)?\\d{9}")
    private String phone;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE})
    @JoinColumn(name = "user_role_id", nullable = false)
    @NotNull
    private Role role;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private Set<Message> messages = new LinkedHashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

}
