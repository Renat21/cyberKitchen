package com.cyber.kitchen.service;


import com.cyber.kitchen.entity.Event;
import com.cyber.kitchen.entity.Member;
import com.cyber.kitchen.entity.User;
import com.cyber.kitchen.enumer.Role;
import com.cyber.kitchen.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserService userService;

//    @Autowired
//    TaskService taskService;

    @Autowired
    TeamService teamService;

    public String createEvent(User user, Event event, RedirectAttributes redirectAttributes){
        if (!user.getRoles().contains(Role.ORGANIZER))
            return "error404";

        if (findEventOrganizer(user) != null)
            return "error404";

        event.setExpertToken(UUID.randomUUID().toString());
        event.setMemberToken(UUID.randomUUID().toString());
        event.setOrganizer(userService.findUserById(user.getId()));
        event.setRunning(true);
        redirectAttributes.addAttribute("event", eventRepository.save(event));
        return "indexOrganizers";
    }

    public String enterToEvent(User user, Long eventId, Model model){
        Event event = eventRepository.findEventById(eventId);
        user = userService.findUserById(user.getId());

        if (event == null)
            return "error404";

        if (!event.getRunning())
            return "error404";

        if (event.getExperts().contains(user)){

            return "expertDashboard";
        }

        if (event.getOrganizer().equals(user)){
            model.addAttribute("event", event);
            return "organizerDashboard";
        }

        if (getUsersFromMembers(event.getMembers()).contains(user)){
            return "memberDashboard";
        }
        return "error404";
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
        eventBD.setMaxMembersInTeam(newEvent.getMaxMembersInTeam());
        redirectAttributes.addAttribute("event", eventRepository.save(eventBD));
        return "organizerDashboard";
    }

    public List<Event> findEventsByOrganizer(User user){
        return eventRepository.findEventsByOrganizer(userService.findUserById(user.getId()));
    }

    public List<User> getUsersFromMembers(List<Member> members){
        return members.stream().map(Member::getUser).collect(Collectors.toList());
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


    public Event startEvent(User user){
        Event event = findEventOrganizer(user);
        event.setStartDate(LocalDateTime.now());
        event = eventRepository.save(event);

        teamService.createTeams(event);
//        taskService.openTaskForAllTeams(user);

        return event;
    }

    @Transactional
    public Event save(Event event){
        return eventRepository.save(event);
    }

}
