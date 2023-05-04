package ru.hard2code.GisDatabaseApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hard2code.GisDatabaseApi.model.UserType;

import java.util.Optional;

public interface UserTypeRepository extends JpaRepository<UserType, Long> {
    Optional<UserType> findByType(UserType.Type type);
}