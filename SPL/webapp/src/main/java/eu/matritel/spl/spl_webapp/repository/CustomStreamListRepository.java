package eu.matritel.spl.spl_webapp.repository;


import eu.matritel.spl.spl_webapp.entity.CustomSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomStreamListRepository extends JpaRepository<CustomSessionEntity, String> {

    @Query(value = "SELECT * FROM sessionlist s WHERE s.project_id = ?1 ORDER BY s.id desc",nativeQuery = true)
    List<CustomSessionEntity> findByLatestProjectId(int projectId);



    CustomSessionEntity findTopBySessionIdOrderByIdDesc(String sessionId);
}
