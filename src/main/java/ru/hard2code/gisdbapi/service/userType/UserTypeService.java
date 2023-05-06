package ru.hard2code.gisdbapi.service.userType;

import ru.hard2code.gisdbapi.model.UserType;

public interface UserTypeService {
    UserType save(UserType userType);

    UserType findByType(UserType.Type type);
}
