package ru.hard2code.gisdbapi.service.user;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ru.hard2code.gisdbapi.model.User;
import ru.hard2code.gisdbapi.model.UserType;
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
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find User with id " + id));
    }

    @Override
    public User create(User user) {
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
    public User update(long id, User newUser) {
        try {
            var user = findById(id);
            //TODO: https://www.baeldung.com/spring-data-partial-update
            if (newUser.getUserType() != null) {
                var type = userTypeRepository.findByType(newUser.getUserType().getType())
                        .orElseGet(() -> userTypeRepository.save(newUser.getUserType()));

                user.setUserType(type);
            }

            user.setChatId(newUser.getChatId());
            user.setEmail(newUser.getEmail());
            user.setPhone(newUser.getPhone());

            return userRepository.save(user);
        } catch (EntityNotFoundException e) {
            return create(newUser);
        }
    }


    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

}
