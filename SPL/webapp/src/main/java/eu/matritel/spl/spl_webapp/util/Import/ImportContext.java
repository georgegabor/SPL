package eu.matritel.spl.spl_webapp.util.Import;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImportContext {
    private ImportStrategy strategy;

    public void setImportStrategy(ImportStrategy strategy) {
        this.strategy = strategy;
    }

    public String[][] getDataAsArray(MultipartFile file) {
        return strategy.parseFile(file);
    }
}
