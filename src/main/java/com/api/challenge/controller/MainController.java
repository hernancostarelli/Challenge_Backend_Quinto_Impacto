package com.api.challenge.controller;

import com.api.challenge.exception.AdminException;
import com.api.challenge.exception.EmailAlreadyExistException;
import com.api.challenge.model.entity.Admin;
import com.api.challenge.model.enums.EExceptionMessage;
import com.api.challenge.service.IAdminService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping({"/", "/index", "/home"})
@RequiredArgsConstructor
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    private final IAdminService adminService;

    @GetMapping({"/", "/index", "/home"})
    public String index() {
        return "index.html";
    }

    @GetMapping({"/login"})
    public String login(@RequestParam(required = false) String error, ModelMap model) {
        if (error != null) {
            model.put("error", EExceptionMessage.INCORRECT_USERNAME_OR_PASSWORD.toString());
        }
        return "login.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_STUDENT')")
    @GetMapping("/index-teacher")
    public String indexTeacher(HttpSession httpSession) {
        Admin admin = (Admin) httpSession.getAttribute("userSession");
        if (admin.getRole().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        return "index-teacher.html";
    }

    @GetMapping({"/register"})
    public String register() {
        return "register.html";
    }

    @PostMapping({"/register"})
    public String register(ModelMap model, @RequestParam String name, @RequestParam String surname, @RequestParam String email,
                           @RequestParam String password, @RequestParam String confirmPassword, RedirectAttributes redirectAttributes) throws EmailAlreadyExistException {
        try {
            adminService.register(name, surname, email, password, confirmPassword);
            model.put("success", EExceptionMessage.THE_USER_HAS_SUCCESSFULLY_REGISTERED.toString());
            return "register.html";
        } catch (Exception exception) {
            logger.info("OCURRIÓ UN ERROR : {}", exception.getMessage());
            model.put("error", exception.getMessage());
            redirectAttributes.addFlashAttribute("name", name);
            redirectAttributes.addFlashAttribute("surname", surname);
            redirectAttributes.addFlashAttribute("email", email);
            return "error.html";
        }
    }
}