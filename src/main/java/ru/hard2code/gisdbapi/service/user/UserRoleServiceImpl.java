package ru.hard2code.gisdbapi.service.user;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ru.hard2code.gisdbapi.model.user.Role;
import ru.hard2code.gisdbapi.repository.UserRoleRepository;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleRepository userRoleRepository;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }


    @Override
    public Role findRoleByName(String name) {
        return userRoleRepository.findByNameIgnoreCase(name).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void createRole(Role type) {
        userRoleRepository.save(type);
    }

    @Override
    public void deleteAllRoles() {
        userRoleRepository.deleteAllInBatch();
    }
}
