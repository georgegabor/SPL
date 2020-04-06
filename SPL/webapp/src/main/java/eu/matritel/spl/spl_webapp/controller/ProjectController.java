package eu.matritel.spl.spl_webapp.controller;

import eu.matritel.spl.spl_webapp.entity.CheckList;
import eu.matritel.spl.spl_webapp.entity.Project;
import eu.matritel.spl.spl_webapp.service.CheckListServiceImpl;
import eu.matritel.spl.spl_webapp.service.ProjectService;
import eu.matritel.spl.spl_webapp.service.SiteServiceImpl;
import eu.matritel.spl.spl_webapp.service.UserService;
import eu.matritel.spl.spl_webapp.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;

import static eu.matritel.spl.spl_webapp.entity.ProjectStatus.*;


@Controller
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private CheckListServiceImpl checkListService;

    @Autowired
    private SiteServiceImpl siteService;

    @GetMapping(value = "/create")
    public String showProjectForm(Model model) {
        Project project = new Project();
        model.addAttribute("project", project);
        model.addAttribute("auditors", userService.getAllAuditors());
        model.addAttribute("engineers", userService.getAllEngineers());
        model.addAttribute("site", siteService.getAllSites());
        model.addAllAttributes(
                PageUtil.mapValues(
                        "New Project",
                        "pages/projects/create",
                        "create_form"));

        return "template";
    }

    @GetMapping(value = "/template")
    public String showProjectTemplateForm(Model model) {
        Project project = new Project();
        model.addAttribute("project", project);
        model.addAttribute("site", siteService.getAllSites());
        model.addAllAttributes(PageUtil.mapValues("New Project Template",
                "pages/projects/template", "template_form"));

        return "template";
    }

    @PostMapping(value = "create")
    public String addProject(Model model, @ModelAttribute("newProject") Project project, BindingResult result) {
        if(result.hasErrors()) {
            System.out.println("errors: \n");
            for(ObjectError oe : result.getAllErrors()) {
                System.out.println(oe.getDefaultMessage());
            }
        } else {
            projectService.create(project);
        }

        return "redirect:customProjectList";
    }

    @PostMapping(value = "template")
    public String addProjectTemplate(Model model, @ModelAttribute("newProjectTemplate") Project project, BindingResult result) {
        if(result.hasErrors()) {
            System.out.println("errors: \n");
            for(ObjectError oe : result.getAllErrors()) {
                System.out.println(oe.getDefaultMessage());
            }
        } else {
            project.setTemplate(true);
            projectService.create(project);
        }
        return "redirect:template/list";
    }

    @GetMapping("customProjectList")
    public String showCustomProjects(Model model) {
        model.addAttribute("customProjectList", projectService.getAllCustomProjects());
        model.addAttribute("site", siteService.getAllSites());
        model.addAllAttributes(PageUtil.mapValues("List of Custom Projects",
                "pages/projects/customProjectList", "customProjectList"));

        return "template";
    }

    @GetMapping("template/list")
    public String showProjectTemplates(Model model) {
        model.addAttribute("projectTemplates", projectService.getAllProjectTemplates());
        model.addAllAttributes(PageUtil.mapValues("List of Project Templates",
                "pages/projects/templateList", "templateList"));

        return "template";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        Project project = projectService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + id));

        model.addAttribute("project", project);
        model.addAttribute("auditors", userService.getAllAuditors());
        model.addAttribute("engineers", userService.getAllEngineers());
        model.addAttribute("site", siteService.getAllSites());
        model.addAllAttributes(
                PageUtil.mapValues(
                        "Update Project",
                        "pages/projects/update",
                        "update_form"));

        return "template";
    }

    @GetMapping("/template/update/{id}")
    public String showUpdateTemplateForm(@PathVariable("id") int id, Model model) {
        Project project = projectService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + id));

        model.addAttribute("project", project);
        model.addAllAttributes(PageUtil.mapValues("Update Project Template",
                "pages/projects/updateTemplate", "updateTemplate_form"));
        return "template";
    }

    @PostMapping("/update/{id}")
    public String updateProject(@PathVariable("id") int id, @Valid Project project,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            project.setId(id);
            return "update";
        }

        projectService.create(project);
        return "redirect:/projects/customProjectList";
    }

    @PostMapping("/template/update/{id}")
    public String updateProjectTemplate(@PathVariable("id") int id, @Valid Project project,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            project.setId(id);
            return "updateTemplate";
        }

        projectService.updateTemplate(project);
        return "redirect:/projects/template/list";
    }

    @GetMapping("/copy/{id}")
    public String copyProject(@PathVariable("id") int id, Model model) {
        Project originalProject = projectService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + id));
        Project newProject = new Project();
        newProject.setName(originalProject.getName());
        newProject.setComment(originalProject.getComment());
        newProject.setAddress(originalProject.getAddress());
        newProject.setAuditor(originalProject.getAuditor());
        newProject.setEngineer(originalProject.getEngineer());
        newProject.setDate(originalProject.getDate());
        newProject.setCheckListSet(new HashSet<>());

        projectService.create(newProject);

        //Copy all checklist item of the original project and add them to the new one.
        for (CheckList checkList : originalProject.getCheckListSet()) {
            //Copy the checklist from the original project and set it's Project field to the new one.
            checkListService.copyCheckList(checkList, newProject);
            newProject.getCheckListSet().add(checkList);
        }

        model.addAttribute("project", newProject);
        model.addAttribute("auditors", userService.getAllAuditors());
        model.addAttribute("engineers", userService.getAllEngineers());
        model.addAllAttributes(
                PageUtil.mapValues(
                        "Update Project",
                        "pages/projects/update",
                        "update_form"));

        return "template";
    }

    @GetMapping("/template/copy/{id}")
    public String copyProjectTemplate(@PathVariable("id") int id, Model model) {
        Project originalProject = projectService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + id));
        Project newProjectTemplate = new Project();
        newProjectTemplate.setName(originalProject.getName());
        newProjectTemplate.setComment(originalProject.getComment());
        newProjectTemplate.setDate(originalProject.getDate());
        newProjectTemplate.setCheckListSet(new HashSet<>());

        newProjectTemplate.setTemplate(true);
        projectService.create(newProjectTemplate);

        //Copy all checklist item of the original project and add them to the new one.
        for (CheckList checkList : originalProject.getCheckListSet()) {
            //Copy the checklist from the original project and set it's Project field to the new one.
            checkListService.copyCheckList(checkList, newProjectTemplate);
            newProjectTemplate.getCheckListSet().add(checkList);
        }

        model.addAttribute("project", newProjectTemplate);
        model.addAllAttributes(PageUtil.mapValues("Update Project Template",
                "pages/projects/updateTemplate", "updateTemplate_form"));

        return "template";
    }

    @GetMapping("/view/{id}")
    public String showCustomProjectDetails(@PathVariable("id") int id, Model model) {
        Project project = projectService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + id));

        model.addAttribute("project", project);
        model.addAllAttributes(PageUtil.mapValues("View Custom Project Details",
                "pages/projects/viewCustomProject", "view_details"));
        return "template";
    }

    @GetMapping("/template/view/{id}")
    public String showProjectTemplateDetails(@PathVariable("id") int id, Model model) {
        Project project = projectService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + id));

        model.addAttribute("project", project);
        model.addAllAttributes(PageUtil.mapValues("View Project Template Details",
                "pages/projects/viewTemplate", "view_details"));
        return "template";
    }

    @GetMapping("/template/createProject/{id}")
    public String createProjectFromTemplate(@PathVariable("id") int id, Model model) {
        Project originalProject = projectService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + id));
        Project newProject = new Project();
        newProject.setName(originalProject.getName());
        newProject.setComment(originalProject.getComment());
        newProject.setDate(originalProject.getDate());
        newProject.setCheckListSet(new HashSet<>());

        newProject.setTemplate(false);
        projectService.create(newProject);
        //Copy all checklist item of the original project and add them to the new one.
        for (CheckList checkList : originalProject.getCheckListSet()) {
            //Copy the checklist from the original project and set it's Project field to the new one.
            checkListService.copyCheckList(checkList, newProject);
            newProject.getCheckListSet().add(checkList);
        }

        model.addAttribute("project", newProject);
        model.addAttribute("auditors", userService.getAllAuditors());
        model.addAttribute("engineers", userService.getAllEngineers());
        model.addAttribute("site", siteService.getAllSites());
        model.addAllAttributes(
                PageUtil.mapValues(
                        "Update Project",
                        "pages/projects/update",
                        "update_form"));

        return "template";
    }

    @GetMapping("/start/{id}")
    public String startCustomProject(@PathVariable("id") int id) {
        Project project = projectService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + id));
        project.setProjectStatus(STARTED);
        projectService.create(project);
        return "redirect:/projects/customProjectList";
    }

    @GetMapping("/archive/{id}")
    public String archiveCustomProject(@PathVariable("id") int id) {
        Project project = projectService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + id));
        project.setProjectStatus(ARCHIVED);
        projectService.create(project);
        return "redirect:/projects/customProjectList";
    }
    @GetMapping("/template/archive/{id}")
    public String archiveProjectTemplate(@PathVariable("id") int id) {
        Project project = projectService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + id));
        project.setProjectStatus(ARCHIVED);
        projectService.create(project);
        return "redirect:/projects/template/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomProject(@PathVariable("id") int id) {
        Project project = projectService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + id));
        project.setProjectStatus(DELETED);
        projectService.create(project);
        return "redirect:/projects/customProjectList";
    }

    @GetMapping("/template/delete/{id}")
    public String deleteProjectTemplate(@PathVariable("id") int id) {
        Project project = projectService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + id));
        project.setProjectStatus(DELETED);
        projectService.create(project);
        return "redirect:/projects/template/list";
    }

    @GetMapping("/overview")
    public String getOverviewPage(Model model){
        model.addAttribute("overview", projectService.getAllCustomProjects());
        model.addAllAttributes(
                PageUtil.mapValues(
                        "Overview Panel",
                        "pages/projects/overview",
                        "overview"));

        return "template";
    }
}
