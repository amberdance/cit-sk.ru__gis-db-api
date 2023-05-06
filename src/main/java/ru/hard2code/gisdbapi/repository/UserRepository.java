package ru.hard2code.gisdbapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hard2code.gisdbapi.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}