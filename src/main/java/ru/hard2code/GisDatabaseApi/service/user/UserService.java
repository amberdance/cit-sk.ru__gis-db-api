package ru.hard2code.GisDatabaseApi.service.user;

import ru.hard2code.GisDatabaseApi.model.User;

import java.util.List;

public interface UserService {
    User findById(long id);

    User create(User user);

    User update(long id, User newUser);

    List<User> saveAll(List<User> users);

    List<User> findAll();

    void delete(long id);

    void delete(User user);
}
