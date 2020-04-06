package eu.matritel.spl.spl_webapp.service;

import eu.matritel.spl.spl_webapp.entity.ActiveSessionByProject;

import java.util.List;


public interface ActiveSessionByProjectService {
    List<ActiveSessionByProject> getAll();

    List<ActiveSessionByProject> getAllOrderBySessionActivated();

    ActiveSessionByProject getByProjectId(int projectId);

    ActiveSessionByProject save(ActiveSessionByProject activeSessionByProject);

    void cleanOldActiveSessions();

    ActiveSessionByProject setActive(int id);

    void remove(int id);

    ActiveSessionByProject getLatest();
/*
    ActiveSessionByProject getLastModified();*/

}

