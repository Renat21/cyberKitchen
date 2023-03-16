package com.cyber.kitchen.controller;

import com.cyber.kitchen.entity.Event;
import com.cyber.kitchen.entity.User;
import com.cyber.kitchen.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class IndexController {

    @Autowired
    EventService eventService;


    @GetMapping("/")
    public String getIndex(@AuthenticationPrincipal User user, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("user", user.getName());
        return "indexOrganizers";
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
