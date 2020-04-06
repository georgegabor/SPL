package eu.matritel.spl.spl_webapp.service;

import eu.matritel.spl.spl_webapp.entity.CheckList;
import eu.matritel.spl.spl_webapp.entity.CheckListSnapshots;
import eu.matritel.spl.spl_webapp.entity.Project;

import java.util.List;
import java.util.Optional;

public interface CheckListService {

     CheckList create(CheckList checkList);
     List<CheckList> getAllCheckLists() ;
     void mapArrayToEntity(String[][] array, Project parentProject);
     Optional<CheckList> findById (Integer id);
     List<CheckList> getProjectCheckLists(Integer id);
     CheckListSnapshots addSnapshot(CheckListSnapshots snapshot);
     List<CheckListSnapshots> getSnapshotsForChecklistItems(int checklistId);



     List<CheckList> findByProjectIdAndAuditor(int projectId ,int userId);
}
