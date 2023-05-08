package ru.hard2code.gisdbapi.service.user;


import org.springframework.stereotype.Service;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.model.User;
import ru.hard2code.gisdbapi.repository.UserRepository;
import ru.hard2code.gisdbapi.repository.UserTypeRepository;
import ru.hard2code.gisdbapi.service.userType.UserTypeService;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserTypeRepository userTypeRepository;


    private final UserTypeService userTypeService;

    public UserServiceImpl(UserRepository userRepository, UserTypeRepository userTypeRepository, UserTypeService userTypeService) {
        this.userRepository = userRepository;
        this.userTypeRepository = userTypeRepository;
        this.userTypeService = userTypeService;
    }

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
    public User update(long id, User newUser) {
        var user = userRepository.findById(id).orElseGet(() -> userRepository.save(newUser));
        var userType = newUser.getUserType();

        user.setUserName(newUser.getUserName());
        user.setFirstName(newUser.getFirstName());
        user.setPhone(newUser.getPhone());
        user.setEmail(newUser.getEmail());
        user.setChatId(newUser.getChatId());
        user.setUserType(userTypeRepository.findById(userType.getId()).orElseThrow(() -> new EntityNotFoundException(userType)));

        return userRepository.save(user);
    }


    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

}
