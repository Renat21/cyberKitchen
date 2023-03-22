package com.cyber.kitchen.service;


import com.cyber.kitchen.entity.Event;
import com.cyber.kitchen.entity.Member;
import com.cyber.kitchen.entity.Team;
import com.cyber.kitchen.entity.User;
import com.cyber.kitchen.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExpertService {
    @Autowired
    UserService userService;

    @Autowired
    EventService eventService;

    @Autowired
    TeamRepository teamRepository;

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
            int current = teamRepository.findTeamsByExpert(expert).size();
            if (current < min)
                freeExpert = expert;
        }

        team.setExpert(freeExpert);
        teamRepository.save(team);
    }

}
