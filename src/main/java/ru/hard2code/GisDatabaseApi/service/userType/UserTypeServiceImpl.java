package ru.hard2code.GisDatabaseApi.service.userType;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ru.hard2code.GisDatabaseApi.model.UserType;
import ru.hard2code.GisDatabaseApi.repository.UserTypeRepository;

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
        return userTypeRepository.findByType(name).orElseThrow(() -> new EntityNotFoundException("UserType with name " + name + " did not found"));
    }
}
