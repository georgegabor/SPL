package eu.matritel.spl.spl_webapp.service;

import eu.matritel.spl.spl_webapp.dto.StreamDTO;
import eu.matritel.spl.spl_webapp.entity.CustomSessionEntity;

import java.util.List;

public interface CustomStreamService {
    String generateToken(int id);
    String getApiKey();
    String getSessionId(StreamDTO streamDTO);
    CustomSessionEntity save (CustomSessionEntity s);
    String getApiSecret();
    CustomSessionEntity getSessionEntityBySessionId(String sessionId);
   // CustomSessionEntity createSession();
    CustomSessionEntity createSessionForProject(int id,boolean isNewSession);
    List<CustomSessionEntity> getAllSessions();
    void saveCheckListItem(int id, String json);
    StreamDTO getNewStreamInDTO(int projectId,boolean isNewSession);

}
