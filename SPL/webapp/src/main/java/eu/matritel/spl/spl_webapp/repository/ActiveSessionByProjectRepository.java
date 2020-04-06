package eu.matritel.spl.spl_webapp.repository;

import eu.matritel.spl.spl_webapp.entity.ActiveSessionByProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActiveSessionByProjectRepository  extends JpaRepository<ActiveSessionByProject, Object> {
    List<ActiveSessionByProject> findAll();
    ActiveSessionByProject getByProjectId(int projectId);

    @Query(value =
            "select * from webapp.active_session_by_project order by session_activated desc",
            nativeQuery = true)
    List<ActiveSessionByProject> findAllOrderBySessionActivated();





}
