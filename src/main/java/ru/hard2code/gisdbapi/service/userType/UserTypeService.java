package ru.hard2code.gisdbapi.service.userType;

import ru.hard2code.gisdbapi.model.UserType;

public interface UserTypeService {

    UserType findByType(UserType.Type type);

    UserType findOrCreateUserType(UserType userType);
}
