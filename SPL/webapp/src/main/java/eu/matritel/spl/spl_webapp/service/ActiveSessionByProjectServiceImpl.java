package eu.matritel.spl.spl_webapp.service;

import eu.matritel.spl.spl_webapp.entity.ActiveSessionByProject;
import eu.matritel.spl.spl_webapp.entity.CustomSessionEntity;
import eu.matritel.spl.spl_webapp.repository.ActiveSessionByProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ActiveSessionByProjectServiceImpl implements ActiveSessionByProjectService {

    private  ActiveSessionByProject activeSessionByProject;

    @Autowired
    private ActiveSessionByProjectRepository activeSessionByProjectRepository;
    @Autowired
    private CustomStreamService customStreamService;

    @Override
    public List<ActiveSessionByProject> getAll() {
        return activeSessionByProjectRepository.findAll();
    }

    @Override
    public List<ActiveSessionByProject> getAllOrderBySessionActivated() {
        return activeSessionByProjectRepository.findAllOrderBySessionActivated();
    }

    @Override
    public ActiveSessionByProject getByProjectId(int projectId) {
        //this is not called directly just by setActive and the null is handled there
        return activeSessionByProjectRepository.getByProjectId(projectId);
    }


    public ActiveSessionByProject setActive(int projectId) {
        if (null == getByProjectId(projectId)) {
            activeSessionByProject = new ActiveSessionByProject();
            CustomSessionEntity cse =customStreamService.createSessionForProject(projectId,true);
           String sessionId =cse.getSessionId();

            activeSessionByProject.setProjectId(projectId);
            activeSessionByProject.setSessionId(sessionId);
        } else {
            activeSessionByProject = getByProjectId(projectId);
        }
        activeSessionByProject.setSessionActivated(new Date(System.currentTimeMillis()));
        save(activeSessionByProject);
        return activeSessionByProject;
    }

    @Override
    public void remove(int id) {
        activeSessionByProjectRepository.delete(activeSessionByProjectRepository.getByProjectId(id));
    }


    @Override
    public ActiveSessionByProject save(ActiveSessionByProject activeSessionByProject) {
        return activeSessionByProjectRepository.save(activeSessionByProject);
    }

    @Override
    public void cleanOldActiveSessions() {
        //clean session after this hour :
        int hour = 1;
        long timeInMilis = 36000000 * hour;

        if (!(getAll().isEmpty())) {
            ArrayList<ActiveSessionByProject> activeSessionByProjects = (ArrayList<ActiveSessionByProject>) getAll();
            for (int i = 0; i < activeSessionByProjects.size(); i++) {
                if (-1 == (activeSessionByProjects.get(i).getSessionActivated().compareTo(new Date(System.currentTimeMillis() - timeInMilis)))) {
                    activeSessionByProjectRepository.delete(activeSessionByProjects.get(i));
                }
            }
        }


    }
@Override
    public ActiveSessionByProject getLatest() {
        return getAllOrderBySessionActivated().get(0);
    }


}
