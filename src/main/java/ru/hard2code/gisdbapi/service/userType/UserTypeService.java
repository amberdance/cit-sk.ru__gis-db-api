package ru.hard2code.gisdbapi.service.userType;

import ru.hard2code.gisdbapi.model.UserType;

public interface UserTypeService {

    UserType findRoleByName(String name);

    UserType createRole(UserType type);

    void deleteAllRoles();
}
