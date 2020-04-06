package eu.matritel.spl.spl_webapp.controller;

import eu.matritel.spl.spl_webapp.entity.CheckList;
import eu.matritel.spl.spl_webapp.entity.Project;
import eu.matritel.spl.spl_webapp.security.service.SecurityService;
import eu.matritel.spl.spl_webapp.service.CheckListService;
import eu.matritel.spl.spl_webapp.service.ProjectService;
import eu.matritel.spl.spl_webapp.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping ("/checklists")
public class CheckListController {

    @Autowired
    private CheckListService checkListService;

    @Autowired
    private ProjectService projectService;

    @GetMapping("create")
    public String showCheckListForm(Model model) {
        CheckList checkList = new CheckList();
        model.addAttribute("checklist", checkList);
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAllAttributes(PageUtil.mapValues("New Checklist",
                "pages/checklists/create", "create_form"));

        return "template";
    }

    @PostMapping(value = "create")
    public String addCheckList(@ModelAttribute CheckList checkList) {
        checkListService.create(checkList);
        return "redirect:list";
    }

    @GetMapping("list")
    public String showCheckLists(Model model) {
        model.addAttribute("checklist", checkListService.getAllCheckLists());
        model.addAllAttributes(PageUtil.mapValues("List of Checklists",
                "pages/checklists/list", "list"));
        return "template";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@RequestParam("previewPage") Optional<String> previewPage, @PathVariable("id") int id, Model model) {
        CheckList checkList = checkListService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Checklist Id:" + id));
        if(previewPage.isPresent()) {
            System.out.println("prev: " + previewPage.get());
            model.addAttribute("previewPage", previewPage.get());
        }
        model.addAttribute("checklist", checkList);

        model.addAttribute("projects", projectService.getAllProjects());
        model.addAllAttributes(
                PageUtil.mapValues(
                        "Update Checklist",
                        "pages/checklists/update",
                        "update_form"));

        return "template";
    }

    @PostMapping("/update/{id}")
    public String updateCheckList(@RequestParam("previewPage") String previewPage, @PathVariable("id") int id, @Valid CheckList checkList,
                                  BindingResult result, Model model) {
        if (result.hasErrors()) {
            checkList.setId(id);
            return "update";
        }

        checkListService.create(checkList);
        if (previewPage.equals("list")) {
            return "redirect:/checklists/list";
        } else {
            Project project = checkListService.findById(id).get().getProject();
            model.addAttribute("project", project);
            model.addAllAttributes(PageUtil.mapValues("View Project Template Details",
                    "pages/projects/viewTemplate", "view_details"));
            return "template";
        }
    }

//    @GetMapping("/updateCustomProjectChecklist/{id}")
//    public String updateCustomProjectChecklist(@PathVariable("id") int id, @Valid CheckList checkList,
//                                  BindingResult result, Model model) {
//        if (result.hasErrors()) {
//            checkList.setId(id);
//            return "update";
//        }
//
//        //checkListService.create(checkList);
//
//        Project project = checkListService.findById(id).get().getProject();
//
//        model.addAttribute("project", project);
//        model.addAllAttributes(PageUtil.mapValues("View Custom Project Details",
//                "pages/projects/viewCustomProject", "view_details"));
//        return "template";
//    }
//
//    @GetMapping("/updateProjectTemplateChecklist/{id}")
//    public String updateProjectTemplateChecklist(@PathVariable("id") int id, @Valid CheckList checkList,
//                                               BindingResult result, Model model) {
//        if (result.hasErrors()) {
//            checkList.setId(id);
//            return "update";
//        }
//
//        //checkListService.create(checkList);
//
//        Project project = checkListService.findById(id).get().getProject();
//
//        model.addAttribute("project", project);
//        model.addAllAttributes(PageUtil.mapValues("View Project Template Details",
//                "pages/projects/viewTemplate", "view_details"));
//        return "template";
//    }
}
