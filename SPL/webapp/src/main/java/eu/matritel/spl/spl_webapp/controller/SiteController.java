package eu.matritel.spl.spl_webapp.controller;

import eu.matritel.spl.spl_webapp.entity.Site;
import eu.matritel.spl.spl_webapp.entity.User;
import eu.matritel.spl.spl_webapp.service.SiteService;
import eu.matritel.spl.spl_webapp.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/sites")
public class SiteController {

    @Autowired
    private SiteService siteService;

    @GetMapping(value = "/create")
    public String showSiteForm(Model model) {
        Site site = new Site();
        model.addAttribute("site", site);
        model.addAllAttributes(PageUtil.mapValues("New Site",
                "pages/sites/create", "create_form"));

        return "template";
    }

    @PostMapping(value = "create")
    public String addSite(Model model, @ModelAttribute("newSite") Site site, BindingResult result) {
        if(result.hasErrors()) {
            System.out.println("errors: \n");
            for(ObjectError oe : result.getAllErrors()) {
                System.out.println(oe.getDefaultMessage());
            }
        } else {
            siteService.create(site);
        }
        return "redirect:list";
    }

    @GetMapping("list")
    public String showSiteLists(Model model) {
        model.addAttribute("site", siteService.getAllSites());
        model.addAllAttributes(PageUtil.mapValues("List of Sites",
                "pages/sites/list", "list"));
        return "template";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        Site site = siteService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid site Id:" + id));

        model.addAttribute("site", site);
        model.addAllAttributes(PageUtil.mapValues("Update Site",
                "pages/sites/update", "update_form"));
        return "template";
    }

    @PostMapping("/update/{id}")
    public String updateSite(@PathVariable("id") int id, @Valid Site site,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            site.setId(id);
            return "update";
        }

        siteService.create(site);

        return "redirect:/sites/list";
    }
}
