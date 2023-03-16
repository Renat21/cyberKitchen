package com.cyber.kitchen.repository;


import com.cyber.kitchen.entity.Solution;
import com.cyber.kitchen.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long> {
    Solution findSolutionById(Long id);

    List<Solution> findSolutionsByTeam(Team team);


}
