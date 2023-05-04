package ru.hard2code.GisDatabaseApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hard2code.GisDatabaseApi.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}