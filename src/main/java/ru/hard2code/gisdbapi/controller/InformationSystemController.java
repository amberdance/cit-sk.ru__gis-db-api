package ru.hard2code.gisdbapi.controller;

import org.springframework.web.bind.annotation.*;
import ru.hard2code.gisdbapi.model.InformationSystem;
import ru.hard2code.gisdbapi.service.informationSystem.InformationSystemService;

import java.util.List;

@RestController
@RequestMapping("/information-systems")
public class InformationSystemController {

    private final InformationSystemService informationSystemService;

    public InformationSystemController(InformationSystemService informationSystemService) {
        this.informationSystemService = informationSystemService;
    }

    @PostMapping
    public InformationSystem createGis(@RequestBody InformationSystem gis) {
        return informationSystemService.createInformationSystem(gis);
    }

    @GetMapping
    public List<InformationSystem> getAll() {
        return informationSystemService.findAll();
    }

    @GetMapping("/{id}")
    InformationSystem getById(@PathVariable("id") long id) {
        return informationSystemService.findById(id);
    }

    @PutMapping("/{id}")
    InformationSystem update(@PathVariable("id") long id, @RequestBody InformationSystem informationSystem) {
        return informationSystemService.update(id, informationSystem);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") long id) {
        informationSystemService.delete(id);
    }

}
