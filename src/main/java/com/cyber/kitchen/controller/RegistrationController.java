package com.cyber.kitchen.controller;


import com.cyber.kitchen.entity.User;
import com.cyber.kitchen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrationController {
    @Autowired
    UserService userService;

    @GetMapping("/registration")
    public String getRegistration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute(name = "user") User user,
                               @ModelAttribute(name = "confirmPassword") String confirmPassword,
                               @ModelAttribute(name = "yourRole") String yourRole,
                               RedirectAttributes redirectAttributes){
        return userService.registerUser(user, confirmPassword, yourRole, redirectAttributes);
    }
}
