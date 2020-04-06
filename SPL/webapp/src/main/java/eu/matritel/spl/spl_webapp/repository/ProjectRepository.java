package eu.matritel.spl.spl_webapp.repository;

import eu.matritel.spl.spl_webapp.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    @Query(value = "SELECT p FROM project p WHERE p.template = '1' AND (p.projectStatus IS NULL OR p.projectStatus IS NOT '4')")
    List<Project> getAllProjectTemplates();

    @Query(value = "SELECT p FROM project p WHERE NOT p.template = '1' AND (p.projectStatus IS NULL OR p.projectStatus IS NOT '4')")
    List<Project> getAllCustomProjects();

    @Query(value = "SELECT p FROM project p WHERE p.projectStatus IS NULL OR p.projectStatus IS NOT '4'")
    List<Project> findAllByOrderByName();

    List<Project> findAllByAuditorId(int auditorId);
}
