package ru.hard2code.gisdbapi.service.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.model.User;
import ru.hard2code.gisdbapi.repository.UserRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


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
        return userRepository.save(user);
    }

    @Override
    public User updateUser(long id, User newUser) {
        var user = userRepository.findById(id)
                                 .orElseGet(() -> userRepository.save(newUser));

        user.setUserName(newUser.getUserName());
        user.setEmail(newUser.getEmail());
        user.setChatId(newUser.getChatId());
        user.setRole(newUser.getRole());

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
