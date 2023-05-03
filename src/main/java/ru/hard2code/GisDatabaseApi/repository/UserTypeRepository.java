package ru.hard2code.GisDatabaseApi.repository;

import org.springframework.data.repository.CrudRepository;
import ru.hard2code.GisDatabaseApi.model.UserType;

public interface UserTypeRepository extends CrudRepository<UserType, Long> {
}