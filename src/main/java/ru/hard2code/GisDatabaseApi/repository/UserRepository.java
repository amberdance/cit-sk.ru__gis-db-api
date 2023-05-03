package ru.hard2code.GisDatabaseApi.repository;

import org.springframework.data.repository.CrudRepository;
import ru.hard2code.GisDatabaseApi.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
}