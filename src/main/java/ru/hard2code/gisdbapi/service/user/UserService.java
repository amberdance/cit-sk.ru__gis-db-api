package ru.hard2code.gisdbapi.service.user;

import ru.hard2code.gisdbapi.domain.entity.User;

import java.util.List;

public interface UserService {

    User findUserById(long id);

    User createUser(User user);

    User updateUser(long id, User newUser);

    User partialUpdateUser(long id, User newUser);

    List<User> findAllUsers();

    void deleteUserById(long id);


}
