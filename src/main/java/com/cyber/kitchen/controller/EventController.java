package com.cyber.kitchen.controller;


import com.cyber.kitchen.entity.Event;
import com.cyber.kitchen.entity.Team;
import com.cyber.kitchen.entity.User;
import com.cyber.kitchen.service.EventService;
import com.cyber.kitchen.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/event")
public class EventController {

    @Autowired
    EventService eventService;

    @Autowired
    TeamService teamService;

    @GetMapping("/organizer/{eventId}/profile")
    public String getEventForOrganizerProfile(@AuthenticationPrincipal User user, @PathVariable Long eventId, Model model){
        return eventService.enterToEventOrganizer(user, eventId, model, 1);
    }

    @GetMapping("/organizer/{eventId}/theme")
    public String getEventForOrganizerTheme(@AuthenticationPrincipal User user, @PathVariable Long eventId, Model model){
        return eventService.enterToEventOrganizer(user, eventId, model, 2);
    }

    @PostMapping("/organizer/createTask")
    public String createTask(@AuthenticationPrincipal User user, @PathVariable Long eventId, Model model){
        return eventService.enterToEventOrganizer(user, eventId, model, 1);
    }

    @GetMapping("/member/{eventId}")
    public String getEventForMember(@AuthenticationPrincipal User user, @PathVariable Long eventId, Model model){
        return eventService.enterToEventMember(user, eventId, model);
    }

    @PostMapping("/member/enterTeam/{teamId}")
    @ResponseBody
    public String enterTeamMember(@AuthenticationPrincipal User user,
                                  @PathVariable Long teamId,
                                  @ModelAttribute(name = "role") String role){
//        return teamService.enterToTeam(user, teamId, role);
        return "error404";
    }

    @PostMapping("/member/createTeam")
    public String createTeam(@AuthenticationPrincipal User user,
                             @ModelAttribute(name = "team") Team team,
                             @ModelAttribute(name = "role") String role,
                             RedirectAttributes redirectAttributes){
        return eventService.createTeam(user, team, role, redirectAttributes);
    }

    @PostMapping("/member/getTeams")
    @ResponseBody
    public List<Team> getTeams(@AuthenticationPrincipal User user,
                               @ModelAttribute(name = "role") String role){
        return eventService.getTeams(user, role);
    }

    @GetMapping("/expert/{eventId}")
    public String getEventForExpert(@AuthenticationPrincipal User user, @PathVariable Long eventId, Model model){
        return eventService.enterToEventExpert(user, eventId, model);
    }
}
