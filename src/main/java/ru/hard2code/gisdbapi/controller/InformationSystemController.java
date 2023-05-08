package ru.hard2code.gisdbapi.controller;

import org.springframework.http.HttpStatus;
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
    public InformationSystem createInformationSystem(@RequestBody InformationSystem gis) {
        return informationSystemService.createInformationSystem(gis);
    }

    @GetMapping
    public List<InformationSystem> getAllInformationSystems() {
        return informationSystemService.findAll();
    }

    @GetMapping("{id}")
    InformationSystem getInformationSystemById(@PathVariable("id") long id) {
        return informationSystemService.findById(id);
    }

    @PutMapping("{id}")
    InformationSystem updateInformationSystem(@PathVariable("id") long id, @RequestBody InformationSystem informationSystem) {
        return informationSystemService.update(id, informationSystem);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInformationSystem(@PathVariable("id") long id) {
        informationSystemService.delete(id);
    }

}
