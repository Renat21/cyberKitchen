package com.cyber.kitchen.controller;


import com.cyber.kitchen.entity.User;
import com.cyber.kitchen.service.EventService;
import com.cyber.kitchen.service.ExpertService;
import com.cyber.kitchen.service.MemberService;
import com.cyber.kitchen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/event/expert")
public class ExpertController {

    @Autowired
    EventService eventService;
    @Autowired
    ExpertService expertService;

    @GetMapping("/{eventId}/kanban")
    public String getEventForExpert(@AuthenticationPrincipal User user, @PathVariable Long eventId, Model model){
        return eventService.enterToEventExpert(user, eventId, model);
    }

    @PostMapping("/expertEntrance")
    public String enterEvent(@AuthenticationPrincipal User user, @ModelAttribute(name = "token") String token){
        return expertService.expertEntranceEvent(user, token);
    }
}
