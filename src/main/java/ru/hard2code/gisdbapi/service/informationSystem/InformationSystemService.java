package ru.hard2code.gisdbapi.service.informationSystem;

import ru.hard2code.gisdbapi.model.InformationSystem;

import java.util.List;

public interface InformationSystemService {
    InformationSystem createInformationSystem(InformationSystem gis);

    List<InformationSystem> findAll();

    InformationSystem findById(long id);

    void delete(long id);

    InformationSystem update(long id, InformationSystem informationSystem);
}
