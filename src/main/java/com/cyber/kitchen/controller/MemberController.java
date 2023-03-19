package com.cyber.kitchen.controller;


import com.cyber.kitchen.entity.Event;
import com.cyber.kitchen.entity.Team;
import com.cyber.kitchen.entity.User;
import com.cyber.kitchen.repository.EventRepository;
import com.cyber.kitchen.repository.UserRepository;
import com.cyber.kitchen.service.EventService;
import com.cyber.kitchen.service.MemberService;
import com.cyber.kitchen.service.TeamService;
import com.cyber.kitchen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/event/member")
public class MemberController {
    @Autowired
    EventService eventService;
//cbed9856-00ac-425d-be1c-67640dbcdd86
    @Autowired
    MemberService memberService;

    @Autowired
    UserService userService;

    @Autowired
    TeamService teamService;

    @PostMapping("/memberEntrance")
    public String enterEvent(@AuthenticationPrincipal User user, @ModelAttribute(name = "token") String token){
        return memberService.memberEntranceEvent(user, token);
    }


    @GetMapping("/{eventId}/teamProfile")
    public String getEventForMember(@AuthenticationPrincipal User user, @PathVariable Long eventId, Model model){
        return eventService.enterToEventMember(user, eventId, model);
    }

    @PostMapping("/enterTeam")
    @ResponseBody
    public String enterTeamMember(@AuthenticationPrincipal User user,
                                  @RequestBody Map<String, String> json){
        return teamService.enterToTeam(user, Long.parseLong(json.get("teamId")), json.get("role"));
    }

    @PostMapping("/createTeam")
    public String createTeam(@AuthenticationPrincipal User user,
                             @ModelAttribute(name = "team") Team team,
                             @ModelAttribute(name = "role") String role,
                             RedirectAttributes redirectAttributes){
        return eventService.createTeam(user, team, role, redirectAttributes);
    }

    @PostMapping("/addToTeam")
    public String addToTeam(@AuthenticationPrincipal User user,
                            @ModelAttribute(name = "username") String username,
                            @ModelAttribute(name = "role") String role,
                            RedirectAttributes redirectAttributes){
        return memberService.addToTeam(user, username, role, redirectAttributes);
    }

    @PostMapping("/getTeams")
    @ResponseBody
    public List<Team> getTeams(@AuthenticationPrincipal User user,
                               @RequestBody Map<String, String> json){
        return eventService.getTeams(user,  json.get("role"));
    }

    @PostMapping("/exitFromTeam")
    public String exitFromTeam(@AuthenticationPrincipal User user, RedirectAttributes redirectAttributes){
        return memberService.exitFromTeam(user,  redirectAttributes);
    }
}
