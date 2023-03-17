package com.cyber.kitchen.controller;


import com.cyber.kitchen.entity.User;
import com.cyber.kitchen.service.EventService;
import com.cyber.kitchen.service.MemberService;
import com.cyber.kitchen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {
    @Autowired
    EventService eventService;
    @Autowired
    MemberService memberService;

    @PostMapping("/memberEntrance")
    public String enterEvent(@AuthenticationPrincipal User user, @ModelAttribute(name = "token") String token){
        return memberService.memberEntranceEvent(user, token);
    }
}
