package ru.hard2code.gisdbapi.service.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hard2code.gisdbapi.domain.entity.Role;
import ru.hard2code.gisdbapi.domain.entity.User;
import ru.hard2code.gisdbapi.domain.mapper.UserMapper;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.repository.UserRepository;
import ru.hard2code.gisdbapi.service.organization.OrganizationService;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final OrganizationService organizationService;
    private final UserMapper userMapper;


    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        user.setId(null);
        user.setRole(Role.USER);

        if (user.getOrganization().getId() != null) {
            user.setOrganization(organizationService.findOrganizationById(
                    user.getOrganization().getId()));
        }

        return userRepository.save(user);
    }

    @Override
    public User findUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }


    @Override
    public User updateUser(long id, User usr) {
        var optional = userRepository.findById(id);

        if (optional.isEmpty()) {
            return createUser(usr);
        }

        var user = optional.get()
                .toBuilder()
                .organization(usr.getOrganization())
                .role(usr.getRole())
                .username(usr.getUsername())
                .chatId(usr.getChatId())
                .email(usr.getEmail())
                .messages(usr.getMessages())
                .build();

        return userRepository.save(user);
    }

    @Override
    public User partialUpdateUser(long id, User newUser) {
        var updatedUser = userMapper.partialUpdate(userMapper.toDto(newUser),
                findUserById(id));

        return userRepository.save(updatedUser);
    }


    @Override
    public void deleteUserById(long id) {
        userRepository.deleteById(id);
    }

}
