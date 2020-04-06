package eu.matritel.spl.spl_webapp.controller;

import eu.matritel.spl.spl_webapp.service.ArchiveListService;
import eu.matritel.spl.spl_webapp.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/archives")
public class ArchiveListController {

    @Autowired
    private ArchiveListService archiveListService;


    @GetMapping("")
    public String homePage(Model model) {
        model.addAttribute("archiveDtoList", archiveListService.getAllRecordingsInArrayListAsArchiveDTOList());
        model.addAllAttributes(PageUtil.mapValues("Achives List",
                "pages/archives/list", "list"));

        return "template";
    }

}
