package eu.matritel.spl.spl_webapp.service;

import com.opentok.MediaMode;
import com.opentok.OpenTok;
import com.opentok.Session;
import com.opentok.SessionProperties;
import com.opentok.exception.OpenTokException;
import eu.matritel.spl.spl_webapp.dto.StreamDTO;
import eu.matritel.spl.spl_webapp.entity.CustomSessionEntity;
import eu.matritel.spl.spl_webapp.entity.Project;
import eu.matritel.spl.spl_webapp.repository.CustomStreamListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class OpenTokServiceImpl extends OpenTok {

    @Value("${opentok.apiKey}")
    private String apiKey;
    @Value("${opentok.apiSecret}")
    private String apiSecret /* = "99cd86a1d41381f875c6efc21304cb43e7a1d707"*/;
    private CustomSessionEntity customSessionEntity = null;
    @Autowired
    CustomStreamListRepository customStreamListRepository;
    @Autowired
    ProjectService projectService;
    @Autowired
    CustomStreamService customStreamService;

    public OpenTokServiceImpl() {

        super(46396382, "99cd86a1d41381f875c6efc21304cb43e7a1d707");
    }

    public StreamDTO generateStreamDTOForProject(int projectId, boolean isNewSession) {

        Session s = null;
        Project p = projectService.findById(projectId).get();
        String sessionId=null;
        OpenTokServiceImpl o = new OpenTokServiceImpl();
        String token = null;
        if (isNewSession) {
            customSessionEntity = new CustomSessionEntity();
            SessionProperties sessionProperties = new SessionProperties.Builder()
                    .mediaMode(MediaMode.ROUTED)
                    .build();
            try {
                s = o.createSession(sessionProperties);
            } catch (OpenTokException e) {
                e.printStackTrace();
            }
            sessionId = s.getSessionId();
            try {
                token = s.generateToken();
            } catch (OpenTokException e) {
                e.printStackTrace();
            }
            customSessionEntity.setSessionId(s.getSessionId());
            customSessionEntity.setProject(p);
            customStreamService.save(customSessionEntity);

            return new StreamDTO(apiKey, sessionId, token);
        } else {
            customSessionEntity=customStreamListRepository.findByLatestProjectId(projectId).get(0);
            sessionId = customSessionEntity.getSessionId();
            try {
                token = generateToken(sessionId);
            } catch (OpenTokException e) {
                e.printStackTrace();
            }

            return new StreamDTO(apiKey, sessionId, token);
        }
    }


    @Override
    public String generateToken(String sessionId) throws OpenTokException {
        return super.generateToken(sessionId);
    }

    public String generateToken(int projectId) {
        String token = null;
        try {
            token = generateToken(customStreamListRepository.findByLatestProjectId(projectId).get(0).getSessionId());
        } catch (OpenTokException e) {
            e.printStackTrace();
        }

        return token;
    }

}
