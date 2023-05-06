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
    public UserType save(UserType userType) {
        return userTypeRepository.save(userType);
    }

    @Override
    public UserType findByType(UserType.Type name) {
        return userTypeRepository.findByType(name).orElseThrow(() -> new EntityNotFoundException("Cannot find UserType with name " + name));
    }
}
