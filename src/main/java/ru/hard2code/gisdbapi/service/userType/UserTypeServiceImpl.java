package ru.hard2code.gisdbapi.service.userType;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ru.hard2code.gisdbapi.model.UserType;
import ru.hard2code.gisdbapi.repository.UserTypeRepository;

@Service
public class UserTypeServiceImpl implements UserTypeService {
    private final UserTypeRepository userTypeRepository;

    public UserTypeServiceImpl(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }


    @Override
    public UserType findByName(String name) {
        return userTypeRepository.findByNameIgnoreCase(name).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public UserType createUserType(UserType type) {
        return userTypeRepository.save(type);
    }
}
