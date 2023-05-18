package ru.hard2code.gisdbapi.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.hard2code.gisdbapi.constants.Route;
import ru.hard2code.gisdbapi.domain.dto.UserDto;
import ru.hard2code.gisdbapi.domain.entity.User;
import ru.hard2code.gisdbapi.domain.mapper.UserMapper;
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
    public UserDto getUserById(@PathVariable("id") long id) {
        return userMapper.toDto(userService.findUserById(id));
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers()
                          .stream()
                          .map(userMapper::toDto)
                          .collect(Collectors.toList());
    }

    @PostMapping
    public UserDto createUser(@RequestBody User user) {
        return userMapper.toDto(userService.createUser(user));
    }

    @PutMapping("{id}")
    public UserDto updateUser(@PathVariable("id") long id,
                              @RequestBody User user) {
        return userMapper.toDto(userService.updateUser(id, user));
    }

    @PatchMapping("{id}")
    public UserDto partialUpdateUser(@PathVariable("id") long id,
                                     @RequestBody User user) {
        return userMapper.toDto(userService.partialUpdateUser(id, user));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") long id) {
        userService.deleteUserById(id);
    }

}
