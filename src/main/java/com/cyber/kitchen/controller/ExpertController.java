package com.cyber.kitchen.controller;


import com.cyber.kitchen.entity.User;
import com.cyber.kitchen.service.EventService;
import com.cyber.kitchen.service.MemberService;
import com.cyber.kitchen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/event")
public class ExpertController {

    @Autowired
    EventService eventService;
    @Autowired
    MemberService memberService;

    @Autowired
    UserService userService;

}
