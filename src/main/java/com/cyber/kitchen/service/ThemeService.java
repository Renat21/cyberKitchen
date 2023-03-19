package com.cyber.kitchen.service;


import com.cyber.kitchen.entity.Event;
import com.cyber.kitchen.entity.Theme;
import com.cyber.kitchen.entity.User;
import com.cyber.kitchen.repository.EventRepository;
import com.cyber.kitchen.repository.ThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThemeService {

    @Autowired
    EventService eventService;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserService userService;

    @Autowired
    ThemeRepository themeRepository;

    public String createThemeForEvent(User user, Theme theme){
        Event event = eventService.findEventOrganizer(user);
        themeRepository.save(theme);

        event.getThemes().add(theme);
        eventRepository.save(event);

        return "redirect:/event/organizer/" + event.getId() + "/themes";
    }
}