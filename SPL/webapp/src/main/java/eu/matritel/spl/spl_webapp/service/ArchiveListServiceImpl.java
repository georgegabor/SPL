package eu.matritel.spl.spl_webapp.service;

import com.opentok.Archive;
import com.opentok.ArchiveList;
import com.opentok.exception.OpenTokException;
import eu.matritel.spl.spl_webapp.dto.ArchiveDTO;
import eu.matritel.spl.spl_webapp.entity.ArchiveEntity;
import eu.matritel.spl.spl_webapp.entity.CheckList;
import eu.matritel.spl.spl_webapp.entity.CustomSessionEntity;
import eu.matritel.spl.spl_webapp.entity.Project;
import eu.matritel.spl.spl_webapp.repository.ArchiveListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArchiveListServiceImpl implements ArchiveListService {

    @Autowired
    private ArchiveListRepository archiveListRepository;
    @Autowired
    private OpenTokServiceImpl openTokServiceimpl;
    @Autowired
    private CustomStreamService customStreamService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private CheckListService checkListService;


    /*
     * To  produce ArchiveDTO need for the other services getAll
     */


    @Override
    public ArchiveEntity save(ArchiveEntity ArchiveEntity) {
        return archiveListRepository.save(ArchiveEntity);
    }

    @Override
    public ArchiveEntity save(String sessionIdForeignKey) {
        Archive archive;
        ArchiveEntity archiveEntity = new ArchiveEntity();
        try {
            archive = openTokServiceimpl.startArchive(sessionIdForeignKey);
            archiveEntity.setArchiveId(archive.getId());
            archiveEntity.setSessionId(customStreamService.getSessionEntityBySessionId(sessionIdForeignKey));
        } catch (OpenTokException e) {
            e.printStackTrace();
        }
        System.out.println(archiveEntity.getId());
        System.out.println(archiveEntity.getSessionId());
        return save(archiveEntity);
    }

    @Override
    public ArchiveEntity stopRecording(String sessionIdForeignKey) {
        ArchiveEntity archiveEntity;
        Archive archive = null;
        archiveEntity = findBySessionId(sessionIdForeignKey);
        try {
            archive = openTokServiceimpl.stopArchive(archiveEntity.getArchiveId());
        } catch (OpenTokException e) {
            e.printStackTrace();
        }

        return archiveEntity;
    }

    @Override
    public ArchiveEntity findBySessionId(String sessionId) {
        CustomSessionEntity cse = customStreamService.getSessionEntityBySessionId(sessionId);
        int id = cse.getId();
        return archiveListRepository.findTopBySessionIdIdOrderByIdDesc(id);
    }

    @Override
    public List<ArchiveEntity> getAll() {
        return archiveListRepository.findAll();
    }

    @Override
    public void updateArchiveEntitiesUrl() {
        ArrayList<ArchiveEntity> archiveEntityArrayList = (ArrayList<ArchiveEntity>) getAll();
        for (int i = 0; i < archiveEntityArrayList.size(); i++) {
            ArchiveEntity ae = archiveEntityArrayList.get(i);
            if (null != ae.getUrl())
                archiveEntityArrayList.remove(ae);
        }
        try {
            ArchiveList archiveListArrayList = openTokServiceimpl.listArchives();
            for (Archive a : archiveListArrayList
            ) {
                for (int i = 0; i < archiveEntityArrayList.size(); i++) {
                    ArchiveEntity ae = archiveEntityArrayList.get(i);
                    if (ae.getArchiveId().equalsIgnoreCase(a.getId())) {
                        //  System.out.println(ae.getArchiveId() + " " + (a.getId()));
                        ae.setUrl(a.getUrl());
                        archiveListRepository.save(ae);
                    }
                }
            }
        } catch (OpenTokException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<ArchiveDTO> getAllRecordingsInArrayListAsArchiveDTOList() {
        ArrayList<Project> projectArrayList = (ArrayList<Project>) projectService.getAllCustomProjects();
        ArrayList<CheckList> checkListArrayList = (ArrayList<CheckList>) checkListService.getAllCheckLists();
        ArrayList<CustomSessionEntity> customSessionEntityArrayList = (ArrayList<CustomSessionEntity>) customStreamService.getAllSessions();
        ArrayList<ArchiveEntity> archiveEntityArrayList = (ArrayList<ArchiveEntity>) getAll();
        ArrayList<ArchiveDTO> archiveDTOArrayList = new ArrayList<ArchiveDTO>();

        ArchiveDTO archiveDTO= new ArchiveDTO("project","checkList", "session", "archivieId","url");
        archiveDTOArrayList.add(archiveDTO);
        return archiveDTOArrayList;
    }
}
