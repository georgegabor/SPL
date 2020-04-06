package eu.matritel.spl.spl_webapp.controller;

import eu.matritel.spl.spl_webapp.security.service.SecurityService;
import eu.matritel.spl.spl_webapp.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping ("/")
public class WelcomeController {

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAllAttributes(PageUtil.mapValues("Home",
                "pages/home/welcome", "welcome"));

        return "template";
    }

    @GetMapping("/login")
    public String login(Model model)
    {
        return "login";
    }
}
