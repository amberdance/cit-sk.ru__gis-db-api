package ru.hard2code.GisDatabaseApi.service;

import ru.hard2code.GisDatabaseApi.model.User;

import java.util.List;

public interface UserService {
    User findById(Long id);

    User save(User user);

    List<User> saveAll(List<User> users);

    List<User> findAll();
}
