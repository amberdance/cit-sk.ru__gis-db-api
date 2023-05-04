package ru.hard2code.GisDatabaseApi.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ru.hard2code.GisDatabaseApi.model.User;
import ru.hard2code.GisDatabaseApi.model.UserType;
import ru.hard2code.GisDatabaseApi.repository.UserRepository;
import ru.hard2code.GisDatabaseApi.repository.UserTypeRepository;

import java.util.ArrayList;
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
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id " + id + " did not found"));
    }

    @Override
    public User save(User user) {
        var userType = user.getUserType();

        try {
            var userTypeFromDb = userTypeRepository.findByType(userType.getType()).orElseThrow(NullPointerException::new);
            user.setUserType(userTypeFromDb);
        } catch (NullPointerException e) {
            user.setUserType(userTypeRepository.save(new UserType(userType == null ? UserType.Type.CITIZEN : userType.getType())));
        }

        return userRepository.save(user);
    }

    @Override
    public List<User> saveAll(List<User> users) {
        List<User> result = new ArrayList<>();
        users.forEach(user -> result.add(save(user)));

        return result;
    }


    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

}
