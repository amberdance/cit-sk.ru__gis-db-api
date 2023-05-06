package ru.hard2code.GisDatabaseApi.service.informationSystem;

import ru.hard2code.GisDatabaseApi.model.InformationSystem;

import java.util.List;

public interface InformationSystemService {
    InformationSystem createInformationSystem(InformationSystem gis);

    List<InformationSystem> createInformationSystem(List<InformationSystem> systems);

    List<InformationSystem> findAll();

    InformationSystem findById(long id);

    void delete(long id);

    InformationSystem update(long id, InformationSystem informationSystem);
}
