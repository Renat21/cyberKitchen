package com.cyber.kitchen.service;


import com.cyber.kitchen.entity.Solution;
import com.cyber.kitchen.entity.Team;
import com.cyber.kitchen.repository.SolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SolutionService {
    @Autowired
    SolutionRepository solutionRepository;

    public List<Solution> getSolutionsByTeam(Team team){
        return solutionRepository.findSolutionsByTeam(team);
    }

    @Transactional
    public Solution save(Solution solution){
        return solutionRepository.save(solution);
    }
}
