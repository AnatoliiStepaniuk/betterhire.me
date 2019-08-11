package com.sdehunt.controller;

import com.sdehunt.dto.StatsDTO;
import com.sdehunt.repository.SolutionRepository;
import com.sdehunt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

import static java.time.temporal.ChronoUnit.DAYS;

@RestController
public class StatsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SolutionRepository solutionRepository;

    @RequestMapping(method = RequestMethod.GET, path = "/stats")
    public StatsDTO getStats() {
        Instant now = Instant.now();
        return new StatsDTO()
                .setUsers(userRepository.getTotalUsers()) // TODO should be cached
                .setSolutions(solutionRepository.getTotalSolutions())// TODO should be cached
                .setWau(userRepository.getActiveUsersInRange(now.minus(7, DAYS), now));// TODO should be cached
    }

}
