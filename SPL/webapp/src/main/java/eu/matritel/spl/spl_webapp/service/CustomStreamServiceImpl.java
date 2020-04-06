package eu.matritel.spl.spl_webapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.matritel.spl.spl_webapp.dto.StreamDTO;
import eu.matritel.spl.spl_webapp.entity.CheckList;
import eu.matritel.spl.spl_webapp.entity.CustomSessionEntity;
import eu.matritel.spl.spl_webapp.entity.Project;
import eu.matritel.spl.spl_webapp.repository.CheckListRepository;
import eu.matritel.spl.spl_webapp.repository.CustomStreamListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;


@Service
@Component
public class CustomStreamServiceImpl implements CustomStreamService {

    @Autowired
    CustomStreamListRepository customStreamListRepository;

    @Autowired
    ProjectService projectService;

    @Value("${opentok.apiKey}")
    private String apiKey;
    @Value("${opentok.apiSecret}")
    private String apiSecret;
    private CustomSessionEntity customSessionEntity = null;
    @Autowired
    private OpenTokServiceImpl openTokServiceImpl;
    @Autowired
    ActiveSessionByProjectService activeSessionByProjectService;
    @Autowired
    private CheckListRepository checkListRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<CustomSessionEntity> getAllSessions() {
        return customStreamListRepository.findAll();
    }

    @Override
    public String generateToken(int projectId) {
        String token = openTokServiceImpl.generateToken(projectId);
        return token;
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }

    @Override
    public String getSessionId(StreamDTO streamDTO) {
        return streamDTO.getSessionId();
    }

    @Override
    public CustomSessionEntity save(CustomSessionEntity s) {
        s.setDate(new Date(System.currentTimeMillis()));
        return customStreamListRepository.save(s);
    }

    public String getApiSecret() {
        return apiSecret;
    }

    @Override
    public CustomSessionEntity getSessionEntityBySessionId(String sessionId) {
        return customStreamListRepository.findTopBySessionIdOrderByIdDesc(sessionId);
    }

    @Override
    public CustomSessionEntity createSessionForProject(int projectId,boolean isNewSession) {
        StreamDTO streamDTO = openTokServiceImpl.generateStreamDTOForProject(projectId, isNewSession);
        customSessionEntity = new CustomSessionEntity();
        Project p = projectService.findById(projectId).get();
        customSessionEntity.setSessionId(streamDTO.getSessionId());
        customSessionEntity.setProject(p);
        save(customSessionEntity);
        customSessionEntity.setToken(streamDTO.getToken());
        return customSessionEntity;
    }

    @Override
    public void saveCheckListItem(int id, String json) {

        CheckList checkListdb = checkListRepository.findById(id).get();
        try {
            CheckList checkList = objectMapper.readValue(json, CheckList.class);

            checkListdb.setComment(checkList.getComment());
            checkListdb.setStatus(checkList.getStatus());
            checkListRepository.save(checkListdb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public StreamDTO getNewStreamInDTO(int projectId ,boolean isNewSession) {
        if (isNewSession) {
            return openTokServiceImpl.generateStreamDTOForProject(projectId, true);
        } else
            return openTokServiceImpl.generateStreamDTOForProject(projectId, false);
    }

}
