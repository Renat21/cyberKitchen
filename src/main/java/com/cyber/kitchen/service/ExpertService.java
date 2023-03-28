package com.cyber.kitchen.service;


import com.cyber.kitchen.entity.*;
import com.cyber.kitchen.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpertService {
    @Autowired
    UserService userService;

    @Autowired
    EventService eventService;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    SolutionService solutionService;

    public String expertEntranceEvent(User user, String token){
        Event event = eventService.findEventByExpertToken(token);

        if (event == null)
            return "error404";
        List<User> experts = event.getExperts();
        experts.add(userService.findUserById(user.getId()));
        eventService.save(event);
        return "redirect:/event/expert/" + event.getId() + "/kanban";
    }

    public void selectExpertFreeForTeam(Event event, Team team){
        int min = 99999;
        User freeExpert = null;
        for (User expert: event.getExperts()){
            int current = teamRepository.findTeamsByExpert(expert.getId(), event.getId()).size();
            if (current < min)
                freeExpert = expert;
        }

        team.setExpert(freeExpert);
        teamRepository.save(team);
    }

    @Transactional
    public List<Team> findTeamsByExpert(User user){
        Event event = userService.findExpertsCurrentEvent(user);
        return teamRepository.findTeamsByExpert(user.getId(), event.getId()).stream().map(i -> teamRepository.findTeamById(i)).collect(Collectors.toList());
    }

    public List<Solution> getTeamKanban(User user, Long teamId){
        Team team = teamRepository.findTeamById(teamId);
        if (team == null)
            return null;

        return solutionService.getSolutionsByTeamExpert(user, team);
    }

}
