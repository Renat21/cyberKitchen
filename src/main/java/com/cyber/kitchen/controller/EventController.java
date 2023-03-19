package com.cyber.kitchen.controller;


import com.cyber.kitchen.entity.Event;
import com.cyber.kitchen.entity.Team;
import com.cyber.kitchen.entity.Theme;
import com.cyber.kitchen.entity.User;
import com.cyber.kitchen.service.*;
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
@RequestMapping("/event/organizer")
public class EventController {

    @Autowired
    EventService eventService;

    @Autowired
    TeamService teamService;


    @Autowired
    MemberService memberService;

    @Autowired
    ThemeService themeService;

    @Autowired
    ExpertService expertService;

    @GetMapping("/{eventId}/profile")
    public String getEventForOrganizerProfile(@AuthenticationPrincipal User user, @PathVariable Long eventId, Model model){
        return eventService.enterToEventOrganizer(user, eventId, model, 1);
    }

    @GetMapping("/{eventId}/themes")
    public String getEventForOrganizerTheme(@AuthenticationPrincipal User user, @PathVariable Long eventId, Model model){
        return eventService.enterToEventOrganizer(user, eventId, model, 2);
    }

    @PostMapping("/createTheme")
    public String createTheme(@AuthenticationPrincipal User user,
                              @ModelAttribute(name="theme") Theme theme){
        return themeService.createThemeForEvent(user, theme);
    }
}
