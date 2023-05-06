package ru.hard2code.GisDatabaseApi.service.informationSystem;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ru.hard2code.GisDatabaseApi.model.InformationSystem;
import ru.hard2code.GisDatabaseApi.repository.InformationSystemRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class InformationSystemImpl implements InformationSystemService {

    private final InformationSystemRepository informationSystemRepository;

    public InformationSystemImpl(InformationSystemRepository informationSystemRepository) {
        this.informationSystemRepository = informationSystemRepository;
    }

    @Override
    public InformationSystem createInformationSystem(InformationSystem gis) {
        return informationSystemRepository.save(gis);
    }

    @Override
    public List<InformationSystem> createInformationSystem(List<InformationSystem> systems) {
        List<InformationSystem> result = new ArrayList<>();
        systems.forEach(system -> result.add(createInformationSystem(system)));

        return result;
    }

    @Override
    public List<InformationSystem> findAll() {
        return informationSystemRepository.findAll();
    }

    @Override
    public InformationSystem findById(long id) {
        return informationSystemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find information system with id " + id));
    }

    @Override
    public void delete(long id) {
        informationSystemRepository.deleteById(id);
    }

    @Override
    public InformationSystem update(long id, InformationSystem informationSystem) {
        var result = informationSystemRepository.findById(id).orElseGet(() -> informationSystemRepository.save(new InformationSystem()));
        result.setName(informationSystem.getName());

        return result;
    }
}
