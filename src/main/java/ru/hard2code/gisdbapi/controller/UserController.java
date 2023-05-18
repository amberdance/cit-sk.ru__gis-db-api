package ru.hard2code.gisdbapi.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.hard2code.gisdbapi.constants.Route;
import ru.hard2code.gisdbapi.dto.UserDto;
import ru.hard2code.gisdbapi.mapper.UserMapper;
import ru.hard2code.gisdbapi.model.User;
import ru.hard2code.gisdbapi.service.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Route.USERS)
@Tag(name = "UserController", description = "User management API")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;


    @GetMapping("{id}")
    public User getUserById(@PathVariable("id") long id) {
        return userService.findUserById(id);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers()
                          .stream()
                          .map(userMapper::map)
                          .collect(Collectors.toList());
    }

    @PostMapping
    public UserDto createUser(@RequestBody User user) {
        return userMapper.map(userService.createUser(user));
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable("id") long id,
                           @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") long id) {
        userService.deleteUserById(id);
    }

}
