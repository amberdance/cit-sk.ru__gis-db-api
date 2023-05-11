package ru.hard2code.gisdbapi.service.user;

import ru.hard2code.gisdbapi.model.user.Role;

public interface UserRoleService {

    Role findRoleByName(String name);

    void createRole(Role type);

    void deleteAllRoles();
}
