package eu.matritel.spl.spl_webapp.service;

import eu.matritel.spl.spl_webapp.entity.Project;
import eu.matritel.spl.spl_webapp.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public Project create(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Project updateTemplate(Project project) {
        project.setTemplate(true);
        return projectRepository.save(project);
    }

    @Override
    public List<Project> getAllCustomProjects() {
        return projectRepository.getAllCustomProjects();
    }

    @Override
    public Optional<Project> findById(int id) {
        return projectRepository.findById(id);
    }

    @Override
    public List<Project> getAllProjectTemplates() {
        return projectRepository.getAllProjectTemplates();
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAllByOrderByName();
    }

    @Override
    public List<Project> findAllByAuditorId(int audtitorId) {
        return projectRepository.findAllByAuditorId(audtitorId);
    }
}
