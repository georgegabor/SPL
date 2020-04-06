package eu.matritel.spl.spl_webapp.service;

import eu.matritel.spl.spl_webapp.entity.CheckList;
import eu.matritel.spl.spl_webapp.entity.CheckListSnapshots;
import eu.matritel.spl.spl_webapp.entity.Project;
import eu.matritel.spl.spl_webapp.repository.CheckListRepository;
import eu.matritel.spl.spl_webapp.repository.CheckListSnapshotsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CheckListServiceImpl implements CheckListService {
    private final String HEADER_CELL_TITLE = "header";

    @Autowired
    private CheckListRepository checkListRepository;

    @Autowired
    private CheckListSnapshotsRepository snapshotRepository;

    public CheckList create(CheckList checkList) {
        return checkListRepository.save(checkList);
    }

    public List<CheckList> getAllCheckLists() {
        return checkListRepository.findAll(sortByOrderAsc());
    }

    private Sort sortByOrderAsc() {
        return new Sort(Sort.Direction.ASC, "project.id").and(new Sort(Sort.Direction.ASC, "order"));
    }

    //Copy checklist. The project argument is the new one, because we want to add it as "value" to the copied checklist's project field.
    //In the case of every other field it's get the value of the copied one. In the case of updatedAt field it would need changed,
    public void copyCheckList(CheckList checkList, Project project) {
        CheckList newCheckList = new CheckList();
        //Date() Allocates a Date object and initializes it so that it represents the time at which it was allocated,
        // measured to the nearest millisecond.
        newCheckList.setCreatedAt(new Date());
        newCheckList.setUpdatedAt(new Date());
        newCheckList.setStatus(checkList.getStatus());
        newCheckList.setOrder(checkList.getOrder());
        newCheckList.setItemNumber(checkList.getItemNumber());
        newCheckList.setHeader(checkList.getHeader());
        newCheckList.setParent(checkList.getParent());
        newCheckList.setName(checkList.getName());
        newCheckList.setDescription(checkList.getDescription());
        newCheckList.setSeverity(checkList.getSeverity());
        newCheckList.setComment(checkList.getComment());
        newCheckList.setProject(project);

        checkListRepository.save(newCheckList);
    }

    @Override
    public void mapArrayToEntity(String[][] stringArray, Project parentProject) {
        int order = 0;
        //if(parentProject.getCheckListSet() != null) {
        //order = parentProject.getCheckListSet().size();
        //}
        int countOfHeader = 0;
        int orderInSection = 0;
        CheckList parentCheckListItem = null;

        for (int i = 0; i < stringArray.length; i++) {
            if (stringArray != null && stringArray[i][0] != null) {
                CheckList checkList = new CheckList();
                checkList.setProject(parentProject);

                if (HEADER_CELL_TITLE.equalsIgnoreCase(stringArray[i][0])) {
                    checkList.setParent(null);
                    parentCheckListItem = checkList;
                    checkList.setHeader(Boolean.TRUE);
                    orderInSection = 0;
                    countOfHeader++;
                } else {
                    checkList.setHeader(Boolean.FALSE);
                    checkList.setSeverity(Integer.valueOf(stringArray[i][0]));
                    if (countOfHeader == 0) {
                        countOfHeader = 1;
                    }
                    checkList.setParent(parentCheckListItem);
                }
                checkList.setDescription(stringArray[i][1]);
                checkList.setOrder(order);
                checkList.setItemNumber(countOfHeader + "." + orderInSection);
                checkListRepository.save(checkList);

                order++;
                orderInSection++;
            }
        }
    }

    @Override
    public Optional<CheckList> findById(Integer id) {
        return checkListRepository.findById(id);
    }

    @Override
    public List<CheckList> getProjectCheckLists(Integer id) {
        return checkListRepository.findByProjectId(id);
    }

    @Override
    public List<CheckListSnapshots> getSnapshotsForChecklistItems(int checklistId) {
        return snapshotRepository.getCheckListSnapshotsByChecklistId(checklistId);
    }

    @Override
    public List<CheckList> findByProjectIdAndAuditor(int projectId, int userId) {
        return checkListRepository.findByProjectIdAndAuditor(projectId,userId);
    }

    @Override
    public CheckListSnapshots addSnapshot(CheckListSnapshots snapshot) {
        return snapshotRepository.save(snapshot);
    }

}
