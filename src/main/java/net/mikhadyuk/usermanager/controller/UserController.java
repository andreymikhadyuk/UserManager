package net.mikhadyuk.usermanager.controller;

import net.mikhadyuk.usermanager.model.User;
import net.mikhadyuk.usermanager.service.SecurityService;
import net.mikhadyuk.usermanager.service.UserService;
import net.mikhadyuk.usermanager.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

//    @RequestMapping(value = "/registration", method = RequestMethod.GET)
//    public String registration(Model model) {
//        model.addAttribute("userForm", new User());
//
//        return "registration";
//    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(userForm);

        securityService.autoLogin(userForm.getUsername(), userForm.getConfirmPassword());

        return "redirect:/user-list";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("error", "Username or password is incorrect.");
        }

        if (logout != null) {
            model.addAttribute("message", "Logged out successfully.");
        }

        return "login";
    }

    @RequestMapping(value = {"/", "/user-list"}, method = RequestMethod.GET)
    public String userList(Model model) {
        model.addAttribute("userList", userService.findAllUsers());
        return "user-list";
    }

    @RequestMapping(value = {"/user-list/{userId}"})
    public String userList(@PathVariable Long userId, @RequestParam("button") String button, Model model) {
        System.out.println(userId);
        if (button.equals("deleteButton")) {
            userService.removeUser(userId);
        } else {
            userService.blockUser(userId);
        }
        model.addAttribute("userList", userService.findAllUsers());
        return "user-list";
    }
}

