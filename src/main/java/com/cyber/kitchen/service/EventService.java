package com.cyber.kitchen.service;


import com.cyber.kitchen.entity.Event;
import com.cyber.kitchen.entity.User;
import com.cyber.kitchen.enumer.Role;
import com.cyber.kitchen.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserService userService;

    public String createEvent(User user, Event event, RedirectAttributes redirectAttributes){
        if (!user.getRoles().contains(Role.ORGANIZER))
            return "error404";


        event.setRunning(true);
        redirectAttributes.addAttribute("event", eventRepository.save(event));
        return "organizerDashboard";
    }

    public String changeEvent(User user, Event newEvent, RedirectAttributes redirectAttributes, Long eventId){
        if (!user.getRoles().contains(Role.ORGANIZER))
            return "error404";

        Event eventBD = eventRepository.findEventById(eventId);

        if (eventBD == null)
            return "error404";
        if (!eventBD.getOrganizer().getId().equals(user.getId()))
            return "error404";

        eventBD.setEndDate(newEvent.getEndDate());
        eventBD.setStartDate(newEvent.getStartDate());
        eventBD.setMaxTeams(newEvent.getMaxTeams());
        eventBD.setMaxMembersInTeam(newEvent.getMaxMembersInTeam());
        redirectAttributes.addAttribute("event", eventRepository.save(eventBD));
        return "organizerDashboard";
    }



    public Event findEventByExpertToken(String token){
        return eventRepository.findEventByExpertToken(token);
    }

    public Event findEventByMemberToken(String token){
        return eventRepository.findEventByMemberToken(token);
    }

    public Event findEventOrganizer(User user){
        return eventRepository.findEventByOrganizer(user);
    }
    
//    public List<Event> findEvent(){
//        return ;
//    }

    @Transactional
    public Event save(Event event){
        return eventRepository.save(event);
    }

}
