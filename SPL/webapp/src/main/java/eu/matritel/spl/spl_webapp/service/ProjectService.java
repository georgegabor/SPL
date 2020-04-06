package eu.matritel.spl.spl_webapp.service;

import eu.matritel.spl.spl_webapp.entity.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectService {

    Project create(Project project);

    Project updateTemplate(Project project);

    List<Project> getAllCustomProjects();

    List<Project> getAllProjectTemplates();

    Optional<Project> findById(int id);

    List<Project> getAllProjects();

    List<Project>findAllByAuditorId (int audtitorId);

}
