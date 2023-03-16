package com.cyber.kitchen.service;


import com.cyber.kitchen.entity.Event;
import com.cyber.kitchen.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpertService {
    @Autowired
    UserService userService;

    @Autowired
    EventService eventService;

    public String expertEntranceEvent(User user, String token){
        Event event = eventService.findEventByExpertToken(token);

        if (event == null)
            return "error404";
        List<User> experts = event.getExperts();
        experts.add(userService.findUserById(user.getId()));
        eventService.save(event);
        return "expertDashboard";
    }

}
