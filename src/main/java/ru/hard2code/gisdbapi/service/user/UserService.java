package ru.hard2code.gisdbapi.service.user;

import ru.hard2code.gisdbapi.model.User;

import java.util.List;

public interface UserService {
    User findUserById(long id);

    User createUser(User user);

    User update(long id, User newUser);

    List<User> findAllUsers();

    void deleteById(long id);

}
