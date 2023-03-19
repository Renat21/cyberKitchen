package com.cyber.kitchen.controller;


import com.cyber.kitchen.entity.Event;
import com.cyber.kitchen.entity.Team;
import com.cyber.kitchen.entity.User;
import com.cyber.kitchen.service.EventService;
import com.cyber.kitchen.service.MemberService;
import com.cyber.kitchen.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/event")
public class EventController {

    @Autowired
    EventService eventService;

    @Autowired
    TeamService teamService;


    @Autowired
    MemberService memberService;


    // ОРГАНИЗАТОР

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





    // УЧАСТНИК
    @GetMapping("/member/{eventId}/teamProfile")
    public String getEventForMember(@AuthenticationPrincipal User user, @PathVariable Long eventId, Model model){
        return eventService.enterToEventMember(user, eventId, model);
    }

    @PostMapping("/member/enterTeam")
    @ResponseBody
    public String enterTeamMember(@AuthenticationPrincipal User user,
                                  @RequestBody Map<String, String> json){
        return teamService.enterToTeam(user, Long.parseLong(json.get("teamId")), json.get("role"));
    }

    @PostMapping("/member/createTeam")
    public String createTeam(@AuthenticationPrincipal User user,
                             @ModelAttribute(name = "team") Team team,
                             @ModelAttribute(name = "role") String role,
                             RedirectAttributes redirectAttributes){
        return eventService.createTeam(user, team, role, redirectAttributes);
    }

    @PostMapping("/member/addToTeam")
    public String addToTeam(@AuthenticationPrincipal User user,
                             @ModelAttribute(name = "username") String username,
                             @ModelAttribute(name = "role") String role,
                             RedirectAttributes redirectAttributes){
        return memberService.addToTeam(user, username, role, redirectAttributes);
    }

    @PostMapping("/member/getTeams")
    @ResponseBody
    public List<Team> getTeams(@AuthenticationPrincipal User user,
                               @RequestBody Map<String, String> json){
        return eventService.getTeams(user,  json.get("role"));
    }

    @PostMapping("/exitFromTeam")
    public String exitFromTeam(@AuthenticationPrincipal User user, RedirectAttributes redirectAttributes){
        return memberService.exitFromTeam(user,  redirectAttributes);
    }





    // ЭКСПЕРТ
    @GetMapping("/expert/{eventId}")
    public String getEventForExpert(@AuthenticationPrincipal User user, @PathVariable Long eventId, Model model){
        return eventService.enterToEventExpert(user, eventId, model);
    }
}
