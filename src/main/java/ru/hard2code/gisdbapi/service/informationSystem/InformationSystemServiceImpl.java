package ru.hard2code.gisdbapi.service.informationSystem;

import org.springframework.stereotype.Service;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.model.InformationSystem;
import ru.hard2code.gisdbapi.repository.InformationSystemRepository;

import java.util.List;

@Service
public class InformationSystemServiceImpl implements InformationSystemService {

    private final InformationSystemRepository informationSystemRepository;

    public InformationSystemServiceImpl(InformationSystemRepository informationSystemRepository) {
        this.informationSystemRepository = informationSystemRepository;
    }

    @Override
    public InformationSystem createInformationSystem(InformationSystem gis) {
        return informationSystemRepository.save(gis);
    }

    @Override
    public List<InformationSystem> findAll() {
        return informationSystemRepository.findAll();
    }

    @Override
    public InformationSystem findById(long id) {
        return informationSystemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(InformationSystem.class, id));
    }

    @Override
    public void delete(long id) {
        informationSystemRepository.deleteById(id);
    }

    @Override
    public InformationSystem update(long id, InformationSystem informationSystem) {
        var is = informationSystemRepository.findById(id).orElseGet(() -> informationSystemRepository.save(informationSystem));
        is.setName(informationSystem.getName());

        return informationSystemRepository.save(is);
    }
}
