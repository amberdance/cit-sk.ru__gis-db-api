package ru.hard2code.GisDatabaseApi.service.informationSystem;

import org.springframework.stereotype.Service;
import ru.hard2code.GisDatabaseApi.model.InformationSystem;
import ru.hard2code.GisDatabaseApi.repository.InformationSystemRepository;

@Service
public class InformationSystemImpl implements InformationSystemService {

    private final InformationSystemRepository repository;

    public InformationSystemImpl(InformationSystemRepository repository) {
        this.repository = repository;
    }

    @Override
    public InformationSystem createGis(InformationSystem gis) {
        return repository.save(gis);
    }
}
