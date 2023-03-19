package com.cyber.kitchen.controller;

import com.cyber.kitchen.entity.Event;
import com.cyber.kitchen.entity.User;
import com.cyber.kitchen.enumer.Role;
import com.cyber.kitchen.service.EventService;
import com.cyber.kitchen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    EventService eventService;

    @Autowired
    UserService userService;


    @GetMapping("/")
    public String getIndex(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("user", user);
        if (user.getRoles().contains(Role.ORGANIZER)) {
            model.addAttribute("events", eventService.findEventsByOrganizer(user));
            return "indexOrganizers";
        } else if (user.getRoles().contains(Role.USER)) {
            Event event = userService.findUsersCurrentEvent(user);
            if (event != null){
                model.addAttribute("event", event);
                return "redirect:/event/member/" + event.getId() + "/teamProfile";
            }
            return "indexMembers";
        } else if (user.getRoles().contains(Role.EXPERT)){
            Event event = userService.findExpertsCurrentEvent(user);
            if (event != null){
                model.addAttribute("event", event);
                return "redirect:/event/expert/" + event.getId() + "/kanban";
            }
            return "indexExperts";
        }
        return "index";
    }

    @PostMapping("/createEvent")
    public String createEvent(@AuthenticationPrincipal User user,
                              @ModelAttribute(name = "event") Event event,
                              RedirectAttributes redirectAttributes){
        return eventService.createEvent(user, event, redirectAttributes);
    }

    @PostMapping("/changeEvent/{eventId}")
    public String changeEvent(@AuthenticationPrincipal User user,
                              @ModelAttribute(name = "event") Event event,
                              RedirectAttributes redirectAttributes,
                              @PathVariable Long eventId){
        return eventService.changeEvent(user, event, redirectAttributes, eventId);
    }
}
