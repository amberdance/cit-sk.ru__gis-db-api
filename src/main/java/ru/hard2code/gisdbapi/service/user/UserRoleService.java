package ru.hard2code.gisdbapi.service.user;

import ru.hard2code.gisdbapi.model.user.Role;

public interface UserRoleService {

    Role findRoleByName(String name);

    Role createRole(Role type);

    void deleteAllRoles();

    Role findById(Long id);
}
