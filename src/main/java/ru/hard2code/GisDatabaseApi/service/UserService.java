package ru.hard2code.GisDatabaseApi.service;

import ru.hard2code.GisDatabaseApi.model.User;

public interface UserService {
    User findById(Long id);

    User save(User user);
}
