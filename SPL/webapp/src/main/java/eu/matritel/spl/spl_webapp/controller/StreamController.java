package eu.matritel.spl.spl_webapp.controller;

import eu.matritel.spl.spl_webapp.entity.Role;
import eu.matritel.spl.spl_webapp.entity.User;
import eu.matritel.spl.spl_webapp.security.service.SecurityService;
import eu.matritel.spl.spl_webapp.service.ActiveSessionByProjectService;
import eu.matritel.spl.spl_webapp.service.CheckListService;
import eu.matritel.spl.spl_webapp.service.ProjectService;
import eu.matritel.spl.spl_webapp.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/stream")
public class StreamController {

    @Autowired
    private CheckListService checkListService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ActiveSessionByProjectService activeSessionByProject;
    @Autowired
    private SecurityService securityService;

    @GetMapping("/{id}")
    public String checklisthomePage(@PathVariable("id") int id, Model model) {
//       User loggedUser = securityService.findLoggedInUser().get();
//       int logedInUserId= loggedUser.getId();
//       int auditorId= projectService.findById(id).get().getAuditor().getId();
//      if (securityService.findLoggedInUser().get().getId()!= projectService.findById(id).get().getAuditor().getId())
//          return "login";
        model.addAttribute("checklist",checkListService.getProjectCheckLists(id));
        model.addAttribute("projectId",(id));
        model.addAllAttributes(PageUtil.mapValues(
                "Stream/Project/Checklist",
                "pages/stream/checklist", "checklist"));
        return "audit_template";
    }

    @GetMapping("")
    public String projectlisthomePage(Model model) {
        Optional<User> loggedUser = securityService.findLoggedInUser();

        if(loggedUser.isPresent() && Role.ADMIN.toString().equals(loggedUser.get().getRole().getRole().toString()) ) {
            model.addAttribute("project", projectService.getAllCustomProjects());
        } else {
            model.addAttribute("project", projectService.findAllByAuditorId(securityService.findLoggedInUser().get().getId()));
        }

        model.addAllAttributes(PageUtil.mapValues("Stream/Project",
                "pages/stream/project", "project"));

        return "template";
    }
}
