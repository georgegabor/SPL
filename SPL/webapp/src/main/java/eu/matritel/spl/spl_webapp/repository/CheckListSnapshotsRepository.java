package eu.matritel.spl.spl_webapp.repository;

import eu.matritel.spl.spl_webapp.entity.CheckListSnapshots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckListSnapshotsRepository extends JpaRepository<CheckListSnapshots, Integer> {

    @Query(value=" SELECT * FROM webapp.checklist_snapshots where checklist_id = ?1",nativeQuery = true)
    List<CheckListSnapshots>getCheckListSnapshotsByChecklistId (int id);
}
