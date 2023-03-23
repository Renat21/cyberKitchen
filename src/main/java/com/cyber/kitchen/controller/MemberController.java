package com.cyber.kitchen.controller;


import com.cyber.kitchen.entity.Event;
import com.cyber.kitchen.entity.Team;
import com.cyber.kitchen.entity.Theme;
import com.cyber.kitchen.entity.User;
import com.cyber.kitchen.repository.EventRepository;
import com.cyber.kitchen.repository.UserRepository;
import com.cyber.kitchen.service.*;
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

    @Autowired
    ThemeService themeService;

    @PostMapping("/memberEntrance")
    public String enterEvent(@AuthenticationPrincipal User user, @ModelAttribute(name = "token") String token){
        return memberService.memberEntranceEvent(user, token);
    }


    @GetMapping("/{eventId}/teamProfile")
    public String getEventForMember(@AuthenticationPrincipal User user, @PathVariable Long eventId, Model model){
        return eventService.enterToEventMember(user, eventId, model, 1);
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

    @GetMapping("/{eventId}/teamTheme")
    public String getEventForMemberTheme(@AuthenticationPrincipal User user, @PathVariable Long eventId, Model model){
        return eventService.enterToEventMember(user, eventId, model, 2);
    }

    @PostMapping("/getTheme/{themeId}")
    @ResponseBody
    public Theme getTheme(@AuthenticationPrincipal User user, @PathVariable Long themeId){
        return themeService.getThemeById(themeId);
    }

    @PostMapping("/selectTheme/{themeId}")
    public String selectTheme(@AuthenticationPrincipal User user, @PathVariable Long themeId){
        return themeService.selectTheme(user, themeId);
    }


    @GetMapping("/{eventId}/kanban")
    public String getEventForMemberKanban(@AuthenticationPrincipal User user, @PathVariable Long eventId, Model model){
        return eventService.enterToEventMember(user, eventId, model, 3);
    }

//    @GetMapping("/{eventId}/teamProfile/teamTheme")
//    public String getEventForMember(@AuthenticationPrincipal User user, @PathVariable Long eventId, Model model){
//        return eventService.enterToEventMember(user, eventId, model);
//    }
}
