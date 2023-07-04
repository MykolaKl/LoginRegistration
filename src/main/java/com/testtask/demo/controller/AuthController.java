package com.testtask.demo.controller;


import com.testtask.demo.Service.UserService;
import com.testtask.demo.dto.UserDto;
import com.testtask.demo.entity.User;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Controller
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/index")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // handler method to handle user registration request
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle register user form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto user,
                               BindingResult result,
                               Model model) {
        User existing = userService.findByEmail(user.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        userService.saveUser(user);
        System.out.println("Registration successful");
        return "redirect:/register?success";
    }


    @GetMapping("/profile")
    public String showUserProfile(Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            User user = userService.findByEmail(email);
            if (user != null) {
                model.addAttribute("user", user);
                System.out.println("User good");
                return "profile";
            }
        }
        return "error";
    }

    @GetMapping("/profile/edit")
    public String showEditProfilePage(Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            User user = userService.findByEmail(email);
            if (user != null) {
                model.addAttribute("user", user);
                System.out.println("User good for edit");
                return "edit";
            }
        }
        return "error";

    }

    @PatchMapping("/profile/edit")
    public String updateUserProfile(@ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        User existingUser = userService.findByEmail(userDto.getEmail());

        if (existingUser == null) {
            System.out.println("Not found");
            return "error";
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "edit";
        }
        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setPassword(userDto.getPassword());


        userService.saveUser(UserDto.fromUser(existingUser));
        System.out.println("Edited");
        return "redirect:/profile?success";
    }


}
