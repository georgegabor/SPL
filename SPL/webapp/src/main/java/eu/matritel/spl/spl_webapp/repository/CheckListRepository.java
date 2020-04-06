package eu.matritel.spl.spl_webapp.repository;

import eu.matritel.spl.spl_webapp.entity.CheckList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckListRepository extends JpaRepository<CheckList, Integer> {
    List<CheckList> findByProjectId(int projectId);


    @Query(value="SELECT \n" +
            "    c.id,\n" +
            "    c.comment,\n" +
            "    c.created_at,\n" +
            "    c.description,\n" +
            "    c.header,\n" +
            "    c.item_number,\n" +
            "    c.name,\n" +
            "    c.ord,\n" +
            "    c.severity,\n" +
            "    c.status,\n" +
            "    c.updated_at,\n" +
            "    c.parent_id,\n" +
            "    c.project_id,\n" +
            "    p.auditor_id AS auditor_ID_in_User_table\n" +
            "FROM\n" +
            "    webapp.checklist AS c\n" +
            "        LEFT JOIN\n" +
            "    webapp.project AS p ON c.project_id = p.id\n" +
            "WHERE\n" +
            "    c.project_id =?1 AND p.auditor_id =?2 Order By ord",nativeQuery = true)
    List<CheckList> findByProjectIdAndAuditor(int projectId ,int userId);

}
