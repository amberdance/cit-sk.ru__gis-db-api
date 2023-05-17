package ru.hard2code.gisdbapi.service.user;


import org.springframework.stereotype.Service;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.model.user.User;
import ru.hard2code.gisdbapi.repository.UserRepository;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository,
                           UserRoleService userRoleService) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
    }

    private final UserRoleService userRoleService;


    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }

    @Override
    public User createUser(User user) {
        var userRoleId = user.getRole().getId();

        if (userRoleId != null) {
            user.setRole(userRoleService.findById(userRoleId));
        }

        return userRepository.save(user);
    }

    @Override
    public User updateUser(long id, User newUser) {
        var user = userRepository.findById(id)
                .orElseGet(() -> userRepository.save(newUser));
        var userRole = newUser.getRole();

        user.setUserName(newUser.getUserName());
        user.setPhone(newUser.getPhone());
        user.setEmail(newUser.getEmail());
        user.setChatId(newUser.getChatId());
        user.setRole(userRoleService.findById(userRole.getId()));

        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void deleteAllUsers() {
        userRepository.deleteAllInBatch();
    }

}
