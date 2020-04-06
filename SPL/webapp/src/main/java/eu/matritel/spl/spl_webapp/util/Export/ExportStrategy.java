package eu.matritel.spl.spl_webapp.util.Export;

import eu.matritel.spl.spl_webapp.entity.CheckList;
import eu.matritel.spl.spl_webapp.entity.Project;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface ExportStrategy {
    public String export(String path, Optional<Project> project, List<CheckList> checkLists);
}
