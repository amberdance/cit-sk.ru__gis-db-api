package ru.hard2code.gisdbapi.service.user;


import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
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
    public @NotNull Role createRole(Role type) {
        userRoleRepository.save(type);
        return type;
    }

    @Override
    public void deleteAllRoles() {
        userRoleRepository.deleteAllInBatch();
    }

    @Override
    public Role findById(Long id) {
        return userRoleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Role.class, id));
    }
}
