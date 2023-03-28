package com.cyber.kitchen.controller;


import com.cyber.kitchen.entity.Message;
import com.cyber.kitchen.entity.Solution;
import com.cyber.kitchen.entity.Team;
import com.cyber.kitchen.entity.User;
import com.cyber.kitchen.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/event/expert")
public class ExpertController {

    @Autowired
    EventService eventService;
    @Autowired
    ExpertService expertService;

    @Autowired
    SolutionService solutionService;
    @GetMapping("/{eventId}/kanban")
    public String getEventForExpert(@AuthenticationPrincipal User user, @PathVariable Long eventId, Model model){
        return eventService.enterToEventExpert(user, eventId, model);
    }

    @PostMapping("/expertEntrance")
    public String enterEvent(@AuthenticationPrincipal User user, @ModelAttribute(name = "token") String token){
        return expertService.expertEntranceEvent(user, token);
    }

    @GetMapping("/getTeams")
    @ResponseBody
    public List<Team> getTeams(@AuthenticationPrincipal User user){
        return expertService.findTeamsByExpert(user);
    }

    @GetMapping("/getTeamKanban/{teamId}")
    @ResponseBody
    public List<Solution> getTeamKanban(@AuthenticationPrincipal User user, @PathVariable Long teamId){
        return expertService.getTeamKanban(user, teamId);
    }

    @PostMapping("/sendMessage")
    @ResponseBody
    public Message sendMessage(@AuthenticationPrincipal User user, @RequestBody Map<String, String> json){
        return solutionService.sendSolutionForCheck(user, Long.parseLong(json.get("id")), json.get("data"));
    }

    @PostMapping("/setStatus/{solutionId}")
    @ResponseBody
    public String setStatus(@PathVariable Long solutionId, @RequestBody Map<String, String> json){
        return solutionService.setSolutionState(solutionId, json.get("state"), Long.parseLong(json.get("curScore")));
    }
}
