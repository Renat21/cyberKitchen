package com.cyber.kitchen.service;


import com.cyber.kitchen.entity.Event;
import com.cyber.kitchen.entity.Member;
import com.cyber.kitchen.entity.Team;
import com.cyber.kitchen.entity.User;
import com.cyber.kitchen.enumer.EventRole;
import com.cyber.kitchen.enumer.Role;
import com.cyber.kitchen.repository.EventRepository;
import com.cyber.kitchen.repository.MemberRepository;
import com.cyber.kitchen.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserService userService;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    MemberRepository memberRepository;

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

    public String enterToEventMember(User user, Long eventId, Model model){
        Event event = eventRepository.findEventById(eventId);
        user = userService.findUserById(user.getId());

        if (event == null)
            return "error404";

        if (!event.getRunning())
            return "error404";

        if (getUsersFromMembers(event.getMembers()).contains(user)){
            Team team = getUsersTeamByEvent(event, user);
            model.addAttribute("event", event);
            if (team != null) {
                return "memberDashboardTeamProfile";
            }
            return "memberDashboardTeamSearch";
        }
        return "error404";
    }

    public String enterToEventExpert(User user, Long eventId, Model model){
        Event event = eventRepository.findEventById(eventId);
        user = userService.findUserById(user.getId());

        if (event == null)
            return "error404";

        if (!event.getRunning())
            return "error404";

        if (event.getExperts().contains(user)){
            return "expertDashboard";
        }
        return "error404";
    }

    public String enterToEventOrganizer(User user, Long eventId, Model model, int page){
        Event event = eventRepository.findEventById(eventId);
        user = userService.findUserById(user.getId());

        if (event == null)
            return "error404";

        if (!event.getRunning())
            return "error404";

        if (event.getOrganizer().equals(user)){
            model.addAttribute("event", event);
            switch (page) {
                case 1:
                    return "organizerDashboardEventProfile";
                case 2:
                    return "organizerDashboardEventThemes";

            }
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

    public Team getUsersTeamByEvent(Event event, User user){
        Member member = memberRepository.findMemberByUser(user);
        for (Team team: event.getTeams())
            if (team.getMembers().contains(member))
                return team;

        return null;
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

    public String createTeam(User user, Team team, String role, RedirectAttributes redirectAttributes){
        user = userService.findUserById(user.getId());
        Event event = userService.findUsersCurrentEvent(user);
        Member member = memberRepository.findMemberByUser(user);

        member.setRole(userService.getEventRole(role));
        teamRepository.save(team);
        team.getMembers().add(member);
        teamRepository.save(team);

        event.getTeams().add(team);
        eventRepository.save(event);
        return "redirect:/event/member/" + event.getId();
    }

    public List<Team> getTeams(User user, String role){
        EventRole eventRole = userService.getEventRole(role);
        Event currentEvent = userService.findUsersCurrentEvent(user);
        List<Team> teams = currentEvent.getTeams();
        List<Team> availableTeams = new ArrayList<>();
        int memberSInTeam;

        for (Team team: teams){
            memberSInTeam = team.getMembers().size();
            Set<EventRole> roles = team.getMembers().stream().map(Member::getRole).collect(Collectors.toSet());

            // 1 место в команде
            if (currentEvent.getMaxMembersInTeam() == memberSInTeam + 1){
                if (roles.size() == 3 || roles.size() == 2 && !roles.contains(eventRole))
                    availableTeams.add(team);
            }

            // 2 места в команде
            else if (currentEvent.getMaxMembersInTeam() == memberSInTeam + 2){
                if (roles.size() == 3 || roles.size() == 2 || roles.size() == 1 && !roles.contains(eventRole))
                    availableTeams.add(team);
            }

            // 3 места в команде
            else if (currentEvent.getMaxMembersInTeam() > memberSInTeam + 2){
                availableTeams.add(team);
            }
        }

        return availableTeams;
    }


    public Event startEvent(User user){
        Event event = findEventOrganizer(user);
        event.setStartDate(LocalDateTime.now());
        event = eventRepository.save(event);

//        teamService.createTeams(event);
//        taskService.openTaskForAllTeams(user);

        return event;
    }

    @Transactional
    public Event save(Event event){
        return eventRepository.save(event);
    }

}
