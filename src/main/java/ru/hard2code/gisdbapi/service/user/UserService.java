package ru.hard2code.gisdbapi.service.user;

import ru.hard2code.gisdbapi.model.User;

import java.util.List;

public interface UserService {
    User findById(long id);

    User create(User user);

    User update(long id, User newUser);

    List<User> findAll();

    void deleteById(long id);

}
