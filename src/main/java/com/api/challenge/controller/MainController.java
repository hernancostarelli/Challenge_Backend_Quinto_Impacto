package com.api.challenge.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/", "/index", "/home"})
public class MainController {

    @GetMapping({"/", "/index", "/home"})
    private String index() {
        return "index.html";
    }

    @GetMapping({"/login"})
    private String login() {
        return "login.html";
    }

    @GetMapping({"/register"})
    private String register() {
        return "register.html";
    }
}