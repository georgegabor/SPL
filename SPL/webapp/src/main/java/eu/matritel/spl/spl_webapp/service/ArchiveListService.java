package eu.matritel.spl.spl_webapp.service;

import eu.matritel.spl.spl_webapp.dto.ArchiveDTO;
import eu.matritel.spl.spl_webapp.entity.ArchiveEntity;

import java.util.List;

public interface ArchiveListService {

    ArchiveEntity save(ArchiveEntity ArchiveEntity);
    ArchiveEntity save(String sessionIdForeignKey);
    ArchiveEntity findBySessionId(String sessionId);
    ArchiveEntity stopRecording(String sessionIdForeignKey);
    List<ArchiveEntity> getAll();
    void updateArchiveEntitiesUrl();
    List <ArchiveDTO> getAllRecordingsInArrayListAsArchiveDTOList();
}
