package ru.hard2code.gisdbapi.service.user;


import org.springframework.stereotype.Service;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.model.User;
import ru.hard2code.gisdbapi.repository.UserRepository;
import ru.hard2code.gisdbapi.repository.UserTypeRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserTypeRepository userTypeRepository;

    public UserServiceImpl(UserRepository userRepository, UserTypeRepository userTypeRepository) {
        this.userRepository = userRepository;
        this.userTypeRepository = userTypeRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }

    @Override
    public User create(User user) {
        var userType = user.getUserType();

        if (userType != null) {
            var existingUserType = userTypeRepository.findByType(userType.getType());

            if (existingUserType.isPresent()) {
                user.setUserType(existingUserType.get());
            } else {
                userTypeRepository.save(userType);
            }
        }


        return userRepository.save(user);
    }

    @Override
    public User update(long id, User newUser) {
        var user = userRepository.findById(id).orElseGet(() -> userRepository.save(newUser));
        var userType = user.getUserType();

        user.setUserName(newUser.getUserName());
        user.setFirstName(newUser.getFirstName());
        user.setPhone(newUser.getPhone());
        user.setEmail(newUser.getEmail());
        user.setChatId(newUser.getChatId());


        if (userType != null) {
            var existingUserType = userTypeRepository.findByType(userType.getType());

            if (existingUserType.isPresent()) {
                user.setUserType(existingUserType.get());
            } else {
                userTypeRepository.save(userType);
            }
        }

        return userRepository.save(user);
    }


    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

}
