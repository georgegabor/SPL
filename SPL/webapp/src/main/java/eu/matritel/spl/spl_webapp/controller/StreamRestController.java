package eu.matritel.spl.spl_webapp.controller;

import eu.matritel.spl.spl_webapp.dto.ArchiveDTO;
import eu.matritel.spl.spl_webapp.dto.SnapshotDTO;
import eu.matritel.spl.spl_webapp.dto.StreamDTO;
import eu.matritel.spl.spl_webapp.entity.ActiveSessionByProject;
import eu.matritel.spl.spl_webapp.entity.ArchiveEntity;
import eu.matritel.spl.spl_webapp.entity.CheckList;
import eu.matritel.spl.spl_webapp.entity.CheckListSnapshots;
import eu.matritel.spl.spl_webapp.service.*;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/streamctrl")
public class StreamRestController {
    @Autowired
    private CustomStreamService customStreamService;
    @Autowired
    private ArchiveListService archiveListService;
    @Autowired
    private ActiveSessionByProjectService activeSessionByProjectService;
    @Autowired
    private IOService ioService;

    @Autowired
    private CheckListService checkListService;

    private static int imgNO = 0;


    @GetMapping("/start_stream/{projectId}")
    public StreamDTO startStream(@PathVariable("projectId") String id, Model model) {
        int projectIdInt = Integer.parseInt(id);
        boolean isThereActiveSessionForProject = true;
        ActiveSessionByProject activeSessionByProject = activeSessionByProjectService.setActive(projectIdInt);
        if (null!=activeSessionByProjectService.getByProjectId(projectIdInt))
        {
             isThereActiveSessionForProject = false;
        }
        StreamDTO dto = customStreamService.getNewStreamInDTO(projectIdInt,isThereActiveSessionForProject);
        activeSessionByProject.setSessionId(dto.getSessionId());
        activeSessionByProjectService.save(activeSessionByProject);
        return dto;
    }

    @PostMapping("/{sessionID}/rec/{chID}")
    public String start(Model model,
                        @PathVariable(value = "sessionID") String sessionID,
                        @PathVariable(value = "chID") String chId) {
        System.out.println("started");
       ArchiveEntity ae= archiveListService.save(sessionID);
        Optional<CheckList> checkListItem = checkListService.findById(Integer.parseInt(chId));
        ae.setCheckListItem(checkListItem.get());
        archiveListService.save(ae);
        return HttpResponseStatus.OK.toString();
    }

    @PutMapping("/{sessionID}/rec/{chID}")
    public ArchiveDTO stop(@PathVariable(value = "sessionID") String sessionID,
                       @PathVariable(value = "chID") String chId) {
        ArchiveEntity ae =  archiveListService.stopRecording(sessionID);

        try {
            Thread.sleep(1900);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArchiveDTO dto = new ArchiveDTO();
        update();
        dto.setUrl(ae.getUrl());
        // dto.setUrl("julis");
        System.out.println(dto.getUrl());
        return dto;
    }

    @PostMapping("/{sessionID}/snap/{chID}")
    public SnapshotDTO snap(
            @PathVariable(value = "sessionID") String sessionID,
            @PathVariable(value = "chID") String chId,
            @RequestBody String imgData) {
        CheckList checkListItem = checkListService.findById(Integer.parseInt(chId)).get();
        String fileName = chId + "_" + System.currentTimeMillis() + ".png";
        ioService.saveFile(fileName, imgData);
        CheckListSnapshots clsn = new CheckListSnapshots();
        clsn.setCheckListItem(checkListItem);
        clsn.setFileName(fileName);

        checkListItem.getSnapshots().add(clsn);
        checkListService.addSnapshot(clsn);

        SnapshotDTO dto = new SnapshotDTO();

        dto.setPictureURL(fileName);

        return dto;
    }

    @PutMapping("/update")
    public String update() {
        archiveListService.updateArchiveEntitiesUrl();
        return HttpResponseStatus.OK.toString();
    }

    @PostMapping(value = "/{checklistId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String checklistButton(@RequestBody String answer,
                                  @PathVariable(value = "checklistId") int checklistId) {
        customStreamService.saveCheckListItem(checklistId, answer);
        return "{}";
    }


}