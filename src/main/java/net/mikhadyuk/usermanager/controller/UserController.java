package net.mikhadyuk.usermanager.controller;

import net.mikhadyuk.usermanager.model.User;
import net.mikhadyuk.usermanager.service.SecurityService;
import net.mikhadyuk.usermanager.service.UserService;
import net.mikhadyuk.usermanager.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @RequestMapping(value = "/login/social", method = RequestMethod.GET)
    public @ResponseBody
    String login(@RequestParam("first_name") String firstName, @RequestParam("email") String email,
                 @RequestParam("uid") String id, Model model) {
        User user = new User();
        user.setName(firstName);
        user.setUsername(email);
        user.setPassword(id.length() < 8 ? id += "12345678" : id);
        user.setConfirmPassword(user.getPassword());

        System.out.println(user);

        userService.save(user);

        securityService.autoLogin(user.getUsername(), user.getConfirmPassword());
        return "user-list";
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        User currentUser = userService.findByUsername(name);
        if (currentUser.isBlocked()) {
            return "/login";
        }

        if (button.equals("deleteButton")) {
            userService.removeUser(userId);
        } else {
            userService.blockOrUnblockUser(userId);
        }
        model.addAttribute("userList", userService.findAllUsers());
        return "user-list";
    }
}

