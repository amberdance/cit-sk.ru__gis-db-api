package ru.hard2code.GisDatabaseApi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hard2code.GisDatabaseApi.model.User;
import ru.hard2code.GisDatabaseApi.repository.UserRepository;

@RestController
@RequestMapping(path = "/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public Iterable<User> getAll() {
        return userRepository.findAll();
    }
}
