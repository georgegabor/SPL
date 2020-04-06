package eu.matritel.spl.spl_webapp.util.Export;

import eu.matritel.spl.spl_webapp.entity.CheckList;
import eu.matritel.spl.spl_webapp.entity.Project;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ExportContext {
    private ExportStrategy strategy;

    public void setExportStrategy(ExportStrategy strategy) {
        this.strategy = strategy;
    }

    public String saveToFile(String path, Optional<Project> project, List<CheckList> checkLists) {
        return strategy.export(path, project, checkLists);
    }
}
