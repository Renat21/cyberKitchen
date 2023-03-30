package com.cyber.kitchen.controller;


import com.cyber.kitchen.entity.User;
import com.cyber.kitchen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    UserService userService;

    @GetMapping("/member")
    public String getProfile(@AuthenticationPrincipal User user, Model model){
        return userService.getMembersProfile(user, model);
    }
}
