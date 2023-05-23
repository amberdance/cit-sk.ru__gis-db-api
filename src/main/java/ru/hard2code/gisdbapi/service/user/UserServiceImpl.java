package ru.hard2code.gisdbapi.service.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
    private final UserMapper userMapper;
    private final OrganizationService organizationService;

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }


    @Override
    public User createUser(User user) {
        user.setId(null);

        if (user.getOrganization().getId() != null) {
            user.setOrganization(organizationService.findById(
                    user.getOrganization().getId()));
        }

        return userRepository.save(user);
    }

    @Override
    public User updateUser(long id, User newUser) {
        var user = findUserById(id)
                .toBuilder()
                .username(newUser.getUsername())
                .email(newUser.getEmail())
                .chatId(newUser.getChatId())
                .role(newUser.getRole());

        if (newUser.getOrganization().getId() != null) {
            user.organization(organizationService.findById(
                    newUser.getOrganization().getId()));
        }

        return userRepository.save(user.build());
    }

    @Override
    public User partialUpdateUser(long id, User newUser) {
        if (newUser.getOrganization().getId() != null) {
            newUser.setOrganization(organizationService.findById(
                    newUser.getOrganization().getId()));
        }

        var updatedUser = userMapper.partialUpdate(userMapper.toDto(newUser),
                findUserById(id));

        return userRepository.save(updatedUser);
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
