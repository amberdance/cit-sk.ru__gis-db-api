package ru.hard2code.gisdbapi.service.userType;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ru.hard2code.gisdbapi.model.UserType;
import ru.hard2code.gisdbapi.repository.UserTypeRepository;

import java.util.Optional;

@Service
public class UserTypeServiceImpl implements UserTypeService {
    private final UserTypeRepository userTypeRepository;

    public UserTypeServiceImpl(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }


    @Override
    public UserType findByType(UserType.Type name) {
        return userTypeRepository.findByType(name).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public UserType findOrCreateUserType(UserType userType) {
        Optional<UserType> existingUserType;

        if (userType.getId() == null) {
            existingUserType = userTypeRepository.findByType(userType.getType());

        } else {
            existingUserType = userTypeRepository.findById(userType.getId());
        }

        return existingUserType.orElseGet(() -> userTypeRepository.save(userType));
    }
}
