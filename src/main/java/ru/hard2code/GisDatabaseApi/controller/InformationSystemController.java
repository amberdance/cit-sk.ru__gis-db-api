package ru.hard2code.GisDatabaseApi.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hard2code.GisDatabaseApi.model.InformationSystem;
import ru.hard2code.GisDatabaseApi.service.informationSystem.InformationSystemService;

@RestController
@RequestMapping("/information-systems")
public class InformationSystemController {

    private final InformationSystemService informationSystemService;

    public InformationSystemController(InformationSystemService informationSystemService) {
        this.informationSystemService = informationSystemService;
    }

    @PostMapping
    public InformationSystem createGis(@RequestBody InformationSystem gis) {
        return informationSystemService.createGis(gis);
    }


}
