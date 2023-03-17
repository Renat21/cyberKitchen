package com.cyber.kitchen.controller;


import com.cyber.kitchen.entity.User;
import com.cyber.kitchen.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class EventController {

    @Autowired
    EventService eventService;

    @GetMapping("/event/{eventId}")
    public String getEvent(@AuthenticationPrincipal User user, @PathVariable Long eventId, Model model){
        return eventService.enterToEvent(user, eventId, model);
    }
}
