package com.cyber.kitchen.service;


import com.cyber.kitchen.entity.Event;
import com.cyber.kitchen.entity.Team;
import com.cyber.kitchen.entity.Theme;
import com.cyber.kitchen.entity.User;
import com.cyber.kitchen.repository.EventRepository;
import com.cyber.kitchen.repository.TeamRepository;
import com.cyber.kitchen.repository.ThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    TaskService taskService;

    @Autowired
    ExpertService expertService;

    public String createThemeForEvent(User user, Theme theme){
        Event event = eventService.findEventOrganizer(user);
        themeRepository.save(theme);

        event.getThemes().add(theme);
        eventRepository.save(event);

        return "redirect:/event/organizer/" + event.getId() + "/themes";
    }

    public String deleteThemeForEvent(User user, Long themeId){
        Theme theme = themeRepository.findThemeById(themeId);
        Event event = eventService.findEventOrganizer(user);

        event.getThemes().remove(theme);
        eventRepository.save(event);

        themeRepository.delete(theme);

        return "redirect:/event/organizer/" + event.getId() + "/themes";
    }

    public String updateThemeForEvent(User user, Long themeId, Theme newTheme){
        Theme theme = themeRepository.findThemeById(themeId);
        Event event = eventService.findEventOrganizer(user);


        theme.setDescription(newTheme.getDescription());
        theme.setName(newTheme.getName());
        themeRepository.delete(theme);

        return "redirect:/event/organizer/" + event.getId() + "/themes";
    }

    public Theme getThemeById(Long id){
        return themeRepository.findThemeById(id);
    }

    public String selectTheme(User user, Long themeId){
        Event event = userService.findUsersCurrentEvent(user);
        Team team = eventService.getUsersTeamByEvent(event, user);
        if (Objects.equals(team.getLeader().getUser().getId(), user.getId())){
            Theme theme = themeRepository.findThemeById(themeId);
            team.setTheme(theme);
            teamRepository.save(team);

            expertService.selectExpertFreeForTeam(event, team);
            taskService.openTaskForTeam(team, event);
            return "redirect:/event/member/" + event.getId() + "/teamTheme";
        }
        return "error404";
    }
}
