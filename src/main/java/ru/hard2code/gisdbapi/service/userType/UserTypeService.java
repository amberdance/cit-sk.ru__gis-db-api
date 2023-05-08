package ru.hard2code.gisdbapi.service.userType;

import ru.hard2code.gisdbapi.model.UserType;

public interface UserTypeService {

    UserType findByName(String name);

    UserType createUserType(UserType type);
}
