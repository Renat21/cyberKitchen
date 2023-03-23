package com.cyber.kitchen.controller;


import com.cyber.kitchen.entity.*;
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
    ThemeService themeService;

    @Autowired
    TaskService taskService;

    @PostMapping("/createEvent")
    public String createEvent(@AuthenticationPrincipal User user,
                              @ModelAttribute(name = "event") Event event,
                              RedirectAttributes redirectAttributes){
        return eventService.createEvent(user, event, redirectAttributes);
    }

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

    @GetMapping("/deleteTheme/{themeId}")
    public String deleteTheme(@AuthenticationPrincipal User user,
                              @PathVariable Long themeId){
        return themeService.deleteThemeForEvent(user, themeId);
    }

//    @PostMapping("/updateTheme/{themeId}")
//    public String updateTheme(@AuthenticationPrincipal User user,
//                              @PathVariable Long themeId,
//                              @ModelAttribute(name = "theme") Theme theme){
//        return themeService.updateThemeForEvent(user, themeId, theme);
//    }

    @GetMapping("/{eventId}/tasks")
    public String getEventForOrganizerTasks(@AuthenticationPrincipal User user, @PathVariable Long eventId, Model model){
        return eventService.enterToEventOrganizer(user, eventId, model, 3);
    }

    @PostMapping("/createTask")
    public String getEventForOrganizerTasks(@AuthenticationPrincipal User user,
                                            @ModelAttribute(name = "task") Task task,
                                            Model model){
        return taskService.createNewTask(user, task);
    }

    @PostMapping("/defineNumeration")
    @ResponseBody
    public String defineNumerationTasks(@AuthenticationPrincipal User user,
                                            @RequestBody Map<Long, Long> numeration){
        return taskService.changeNumerations(user, numeration);
    }

    @GetMapping("/deleteTask/{taskId}")
    public String deleteTask(@AuthenticationPrincipal User user,
                              @PathVariable Long taskId){
        return taskService.deleteTaskForEvent(user, taskId);
    }

    @PostMapping("/changeTask/{taskId}")
    public String changeTasks(@AuthenticationPrincipal User user, @ModelAttribute(name = "task") Task task,
                              @PathVariable Long taskId){
        return taskService.changeTask(user, task, taskId);
    }

    @GetMapping("/{eventId}/members")
    public String getEventForOrganizerMembers(@AuthenticationPrincipal User user, @PathVariable Long eventId, Model model){
        return eventService.enterToEventOrganizer(user, eventId, model, 4);
    }

    @GetMapping("/{eventId}/experts")
    public String getEventForOrganizerExperts(@AuthenticationPrincipal User user, @PathVariable Long eventId, Model model){
        return eventService.enterToEventOrganizer(user, eventId, model, 5);
    }

    @GetMapping("/{eventId}/teams")
    public String getEventForOrganizeTeams(@AuthenticationPrincipal User user, @PathVariable Long eventId, Model model){
        return eventService.enterToEventOrganizer(user, eventId, model, 6);
    }
}
