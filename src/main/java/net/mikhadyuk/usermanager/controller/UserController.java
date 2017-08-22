package net.mikhadyuk.usermanager.controller;

import net.mikhadyuk.usermanager.model.User;
import net.mikhadyuk.usermanager.service.SearchService;
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
    private SearchService searchService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        return "registration";
    }

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

    @RequestMapping(value = {"/", "/user-list"})
    public String userList(@RequestParam(value = "first_name", required=false) String firstName,
                           @RequestParam(value = "email", required=false) String email,
                           @RequestParam(value = "uid", required=false) String id, Model model) {
        if(!(firstName == null && id == null)){
            User user = new User();
            user.setName(firstName);
            user.setUsername(email);
            user.setPassword(id.length() < 8 ? id += "12345678" : id);
            user.setConfirmPassword(user.getPassword());

            userService.save(user);

            securityService.autoLogin(user.getUsername(), user.getConfirmPassword());
        }
        model.addAttribute("userList", userService.findAllUsers());
        return "user-list";
    }

//    @RequestMapping(value = {"/" , "/welcome*"}, method = RequestMethod.GET)
//    public String welcome(@RequestParam(value = "first_name", required=false) String firstName,
//                          @RequestParam(value = "last_name", required=false) String lastName,
//                          @RequestParam(value = "uid", required=false) String id, Model model) {
//        if(firstName == null && id == null){
//            model.addAttribute("userList", userService.getAllUsers());
//        } else {
//            User user = new User();
//            user.setStatus("ACTIVE");
//            user.setPassword(id);
//            String greek
//                    = firstName + lastName;
//            String id2 = "Any-Latin; NFD; [^\\p{Alnum}] Remove";
//            String latin = Transliterator.getInstance(id2)
//                    .transform(greek);
//            user.setUsername(latin);
//            System.out.println(latin);
//
//            userService.save(user);
//
//            securityService.autoLogin(user.getUsername(), user.getPassword());
//
//            model.addAttribute("userList", userService.getAllUsers());
//        }
//        return "welcome";
//
//    }

    @RequestMapping(value = "/user-list/change")
    public String userList(@RequestParam("personId") Long[] userIds, @RequestParam("button") String button, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        User currentUser = userService.findByUsername(name);
        if (currentUser.isBlocked()) {
            return "blocked";
        }

        if (button.equals("deleteButton")) {
            for (long userId : userIds)
                userService.removeUser(userId);
        } else {
            for (long userId : userIds)
                userService.blockOrUnblockUser(userId);
        }
        model.addAttribute("userList", userService.findAllUsers());
        return "user-list";
    }

    @RequestMapping(value = {"/user-list/search"})
    public String Search(@RequestParam(value="value", required = false) String value, Model model ) {
        if (value == null || value.trim().isEmpty() ) {
            return "search";
        }

        model.addAttribute("products", searchService.getRequestedProductsFromBelchip(value));
        return "search";
    }
}

