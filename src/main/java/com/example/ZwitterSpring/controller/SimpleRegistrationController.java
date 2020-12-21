package com.example.ZwitterSpring.controller;

import com.example.ZwitterSpring.domain.User;
import com.example.ZwitterSpring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class SimpleRegistrationController {

    @Autowired
    UserService userService;


    @GetMapping("/registration")
    public String registration () {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model){
        if (!userService.save(user)){
            model.put("message", "User exists");
            return "registration";
        }
        return "redirect:/main";
    }
}
