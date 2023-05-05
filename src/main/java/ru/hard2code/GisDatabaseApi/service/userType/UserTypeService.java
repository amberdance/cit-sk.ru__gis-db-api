package ru.hard2code.GisDatabaseApi.service.userType;

import ru.hard2code.GisDatabaseApi.model.UserType;

public interface UserTypeService {
    UserType save(UserType userType);

    UserType findByType(UserType.Type type);
}
