package com.sdehunt.controller;

import com.sdehunt.dto.StatsDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatsController {

    @RequestMapping(method = RequestMethod.GET, path = "/stats")
    public StatsDTO getStats() {
        return new StatsDTO()
                .setUsers(15)
                .setSolutions(10)
                .setWau(5);
    }

}
