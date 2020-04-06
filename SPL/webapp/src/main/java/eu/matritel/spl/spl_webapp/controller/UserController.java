package eu.matritel.spl.spl_webapp.controller;

import eu.matritel.spl.spl_webapp.entity.User;
import eu.matritel.spl.spl_webapp.security.service.SecurityService;
import eu.matritel.spl.spl_webapp.service.UserService;
import eu.matritel.spl.spl_webapp.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @GetMapping("/create")
    public String showUserForm(Model model, ModelAndView modelAndView) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAllAttributes(PageUtil.mapValues("Add User",
                "pages/users/create", "create_form"));

        return "template";
    }

    @PostMapping("/create")
    public String addUser(@ModelAttribute User user) {
        user.setPassword(securityService.getEncodedText(user.getPassword()));
        userService.create(user);
        return "redirect:list";
    }

    @GetMapping("/list")
    public String showUsersList(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAllAttributes(PageUtil.mapValues("List of Users",
                "pages/users/list", "list"));

        return "template";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        model.addAttribute("user", user);
        model.addAllAttributes(PageUtil.mapValues("Update User",
                "pages/users/update", "update_form"));
        return "template";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") int id, @Valid User user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "update";
        }

        String pw = user.getPassword();
        if(null == pw || pw.isEmpty()) {
            Optional<User> oldUser = userService.findById(id);
            user.setPassword(oldUser.get().getPassword());
        } else {
            user.setPassword(securityService.getEncodedText(user.getPassword()));
        }

        userService.create(user);

        return "redirect:/users/list";
    }
}
