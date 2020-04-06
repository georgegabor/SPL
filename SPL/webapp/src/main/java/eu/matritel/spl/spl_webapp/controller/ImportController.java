package eu.matritel.spl.spl_webapp.controller;

import eu.matritel.spl.spl_webapp.entity.Project;
import eu.matritel.spl.spl_webapp.service.CheckListService;
import eu.matritel.spl.spl_webapp.service.ProjectService;
import eu.matritel.spl.spl_webapp.util.Import.ExcelImportStrategy;
import eu.matritel.spl.spl_webapp.util.Import.ImportContext;
import eu.matritel.spl.spl_webapp.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.http.HttpHeaders;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
@RequestMapping(value="/import")
public class ImportController {

    @Autowired
    ProjectService projectService;

    @Autowired
    CheckListService checkListService;

    @Autowired
    ImportContext importContext;

    @GetMapping(value = "/file")
    public String showProjectForm(Model model) {
        Project project = new Project();
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("project", project);
        model.addAllAttributes(
                PageUtil.mapValues(
                        "Import File",
                        "pages/import/create",
                        "create_form"));

        return "template";
    }

    @PostMapping(value = "/create")
    public String addProject(@ModelAttribute Project project, @ModelAttribute MultipartFile file, Model model){

        importContext.setImportStrategy(new ExcelImportStrategy());
        String[][] stringArray = importContext.getDataAsArray(file);

        checkListService.mapArrayToEntity(stringArray, project);

        return "redirect:/checklists/list";
    }

    @GetMapping(value = "/list")
    public String showProjects(Model model) {
        model.addAttribute("checklist", checkListService.getAllCheckLists());
        model.addAllAttributes(
                PageUtil.mapValues(
                        "Checklist",
                        "pages/import/list",
                        "list"));
        return "template";
    }

    @RequestMapping(path = "/download_file_form", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(String param) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        File file = new File("C:\\Munka\\ProjectPhase\\demo-webapp\\tryItAgain\\webapp\\src\\main\\resources\\static\\images\\file_format.pdf");
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(resource);
    }

}
