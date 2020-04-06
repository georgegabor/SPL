package eu.matritel.spl.spl_webapp.controller;

import eu.matritel.spl.spl_webapp.entity.CheckList;
import eu.matritel.spl.spl_webapp.entity.Project;
import eu.matritel.spl.spl_webapp.service.CheckListService;
import eu.matritel.spl.spl_webapp.service.IOService;
import eu.matritel.spl.spl_webapp.service.ProjectService;
import eu.matritel.spl.spl_webapp.util.Export.ExportContext;
import eu.matritel.spl.spl_webapp.util.Export.PdfExportStrategy;
import eu.matritel.spl.spl_webapp.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/export")
public class ExportController {
    @Autowired
    ProjectService projectService;

    @Autowired
    CheckListService checkListService;

    @Autowired
    ExportContext exportContext;

    @Autowired
    IOService ioService;

    @Autowired
    AttachmentController attachmentController;

    @GetMapping(value = "/file")
    public String showProjectForm(Model model) {
        Project project = new Project();
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("project", project);
        model.addAllAttributes(
                PageUtil.mapValues(
                        "Export File",
                        "pages/export/create",
                        "create_form"));

        return "template";
    }

    @GetMapping(value = "/create")
    @ResponseBody
    public ResponseEntity<Resource> showPdf(@ModelAttribute Project project, final HttpServletResponse response) throws IOException{
        Integer projectId = project.getId();

        Optional<Project> projectToExport = projectService.findById(projectId);
        List<CheckList> checkListsToExport = checkListService.getProjectCheckLists(projectId);

        exportContext.setExportStrategy(new PdfExportStrategy());
        String projectName = exportContext.saveToFile(ioService.getSaveLocation(), projectToExport, checkListsToExport);

        Resource pdf = attachmentController.loadFileAsResource(projectName);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(pdf);
    }
}
